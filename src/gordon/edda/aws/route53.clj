(ns gordon.edda.aws.route53
  (:require [gordon.edda.scala :as s]))

(defn get-host [record]
  (get-in record ["data" "name"]))

(defn hostedRecord->event [record]
  {:host (get-host record) :service "hostedRecords"
   :_id (s/record->_id record)})
