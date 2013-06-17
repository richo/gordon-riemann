(ns edda-stream.core
  (:require [edda-stream.scala :as s]
            [edda-stream.riemann :as r])
  (:import [com.netflix.edda.mongo MongoDatastore]
           [org.joda.time DateTime]
           [scala.collection JavaConversions]))

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

(defn events-since [timestamp & [_type]]
  (find-thing (or _type "aws.instances")
              {"stime" {"$gt" (int->DateTime timestamp)}}))

(defn mainloop [& [since]]
  (let [since (or since (DateTime.))]
    (apply max (map  (fn [event]  (r/create-event event)
                 ; (println (.stime event))
                 (DateTime->int (.stime event)))
     (JavaConversions/asJavaIterable (events-since since))))))


