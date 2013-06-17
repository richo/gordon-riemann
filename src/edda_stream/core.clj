(ns edda-stream.core
  (:require [edda-stream.scala :as s])
  (:import [com.netflix.edda.mongo MongoDatastore]
           [org.joda.time DateTime]
           [scala.collection JavaConversions]))



(defn find-thing [coll where & [limit]]
  (let [datastore (MongoDatastore. coll)]
    (s/scala->clj (.query datastore
                          (s/clj->scala where)
                          (or limit 0)
                          (s/clj->scala #{})
                          true))))

(defn get-instance [id]
 (find-thing "aws.instances" {"_id" id}))

(defn last-instance-event [id]
  (let [where (if (nil? id)
                {}
                {"id" id})]
    (find-thing "aws.instances" where)))

(defn int->DateTime [ts]
  (.toDate (DateTime. ts)))

(defn events-since [timestamp & [_type]]
  (find-thing (or _type "aws.instances")
              {"stime" {"$gt" (int->DateTime timestamp)}}))

(defn create-riemann-event [ev]
  )

(defn mainloop [& [since]]
  (let [since (or since (DateTime.))]
    (max (map (fn [event] (create-riemann-event event)
                (println (get event "stime"))
                (get event "stime"))
              (events-since since)))))
