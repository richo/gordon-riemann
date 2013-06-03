(ns edda-stream.core
  (:require [edda-stream.mongo :as m]))

(defn find-thing []
  (m/connect!)
  (m/find "aws.instances" {:_id ""})) ; FIXME Insert "{reservation_id}|{timestamp}"
