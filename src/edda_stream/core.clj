(ns edda-stream.core
  (:require [edda-stream.mongo :as m]
            [edda-stream.scala :as s])
  (:import [com.netflix.edda.mongo MongoDatastore]))

(defn find-thing []
  (let [datastore (MongoDatastore. "aws.instances")]
    (.query datastore
            (s/clj->scala {"_id" ""}); FIXME Insert "{reservation_id}|{timestamp}"
            0
            (s/clj->scala #{})
            true)))
