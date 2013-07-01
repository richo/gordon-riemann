(ns gordon.edda.core
  (:require [gordon.edda.scala :as s]
            [gordon.edda.riemann :as r])
  (:import [com.netflix.edda.mongo MongoDatastore]
           [org.joda.time DateTime]))

(defonce mongo-datastore
  (memoize (fn [coll] (MongoDatastore. coll))))

(defn find-thing [coll where & [limit]]
  (s/scala->clj (.query (mongo-datastore coll)
                        (s/clj->scala where)
                        (or limit 0)
                        (s/clj->scala #{})
                        true)))

(defn get-instance [id]
 (find-thing "aws.instances" {"_id" id}))

(defn last-instance-event [id]
  (let [where (if (nil? id)
                {}
                {"id" id})]
    (find-thing "aws.instances" where)))

(defn int->DateTime [ts]
  (.toDate (DateTime. ts)))
(defn DateTime->int [ts]
  (.getMillis ts))

(defn events-since [timestamp & [type]]
  (find-thing (or type "aws.instances")
              {"stime" {"$gt" (int->DateTime timestamp)}}))

(defn mainloop [since]
  (let [events (events-since since)]
    (recur (if (seq events)
      (apply max (map (fn [event]
                        (r/create-event event)
                        (DateTime->int (.stime event)))
                      events))
      (do (Thread/sleep 5000) since)))))
