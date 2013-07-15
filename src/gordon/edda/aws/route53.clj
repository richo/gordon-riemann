(ns gordon.edda.aws.route53)

(defn get-host [record]
  (get-in record ["data" "name"]))

(defn hostedRecord->event [record]
  {:host (get-host record) :_table "aws.hostedRecords"})
