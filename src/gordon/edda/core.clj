(ns gordon.edda.core
  (:require [gordon.edda.scala :as s]
            [gordon.edda.riemann :as r]
            [clojure.tools.logging :as log])
  (:import [com.netflix.edda.mongo MongoDatastore]
           [org.joda.time DateTime]))

(defonce mongo-datastore
  (memoize (fn [coll] (MongoDatastore. coll))))

(defn find-in-table [coll where & [limit]]
  (s/scala->clj (.query (mongo-datastore coll)
                        (s/clj->scala where)
                        (or limit 0)
                        (s/clj->scala #{})
                        true)))

(defn int->DateTime [ts]
  (.toDate (DateTime. ts)))
(defn DateTime->int [ts]
  (.getMillis ts))

(defn events-since [timestamp table]
  (find-in-table table
              {"stime" {"$gt" (int->DateTime timestamp)}}))

(defn mainthread [table since]
  (let [events (events-since since table)]
    (recur table (if (seq events)
      (apply max (map (fn [event]
                        (log/warn "Dispatching event " event)
                        (r/create-event table event)
                        (DateTime->int (.stime event)))
                      events))
      (do (Thread/sleep 5000) since)))))

; Shamelessly lifted from riemann
(defn handle-signals
  "Sets up POSIX signal handlers."
  []
  (if (not (.contains (. System getProperty "os.name") "Windows"))
    (sun.misc.Signal/handle
      (sun.misc.Signal. "INT")
      (proxy [sun.misc.SignalHandler] []
        (handle [sig]
          (log/warn "Caught SIGINT, burning world state with fire")
          (System/exit 0))))))

(defn mainloop [tables since]
  (let [threads (map (fn [table]
                       (Thread. (fn [] (mainthread table since)))) tables)]
    (handle-signals)
    (doseq [thread threads] (.start thread))
    (doseq [thread threads] (.join thread))))
