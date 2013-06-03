(ns edda-stream.mongo
  (:require [monger.core :as mongo]
            [monger.collection :as coll]))

(defonce connect!
  (memoize
   (fn []
     (mongo/connect!)
     (mongo/set-db! (mongo/get-db "edda")))))

(defn find [collection criteria]
  (coll/find-one collection criteria))
