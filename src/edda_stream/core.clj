(ns edda-stream.core
  (:require [edda-stream.scala :as s])
  (:import [com.netflix.edda.mongo MongoDatastore]
           [org.joda.time DateTime]
           [scala.collection JavaConversions]))



(defn find-thing [coll where & [limit]]
  (let [datastore (MongoDatastore. coll)]
    (.query datastore
            (s/clj->scala where)
            (or limit 0)
            (s/clj->scala #{})
            true)))
