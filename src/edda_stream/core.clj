(ns edda-stream.core
  (:require [edda-stream.scala :as s]
            [riemann.client :as r])
  (:import [com.netflix.edda.mongo MongoDatastore]
           [org.joda.time DateTime]
           [scala.collection JavaConversions]))

(defonce riemann-client
  (memoize (fn [] (r/tcp-client :host "127.0.0.1"))))

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
(defn DateTime->int [ts]
  (.getMillis ts))

(defn events-since [timestamp & [_type]]
  (find-thing (or _type "aws.instances")
              {"stime" {"$gt" (int->DateTime timestamp)}}))

(defn create-riemann-event [ev]
  )

(defn mainloop [& [since]]
  (let [since (or since (DateTime.))]
    (apply max (map  (fn [event]  (create-riemann-event event)
                 ; (println (.stime event))
                 (DateTime->int (.stime event)))
     (JavaConversions/asJavaIterable (events-since since))))))
