(ns gordon.edda.aws.ec2
  (:use [gordon.riemann.aws.credentials :as credentials])
  (:import [com.amazonaws.services.ec2 AmazonEC2Client]))

(defn get-instance-state [instance]
  (get-in (first (get instance "instances")) ["state" "name"]))

(defn get-instance-name [instance]
  (get (first (get instance "instances")) "instanceId"))

(defn extract-tags [tags]
  (into {} (map (fn [t]
             [(get t "key") (get t "value")])
       tags)))

(defn get-instance-role [instance]
  (get (extract-tags (get (first (get instance "instances")) "tags")) "Role"))

(defn get-public-dns [instance]
  (get (first (get instance "instances")) "publicDnsName"))

(defn state->metric [state]
  (case state
        "running" 0
        "terminated" 1
        "stopping" 2
        "stopped" 4
        "shutting-down" 8
        "pending" 16))

(defn instance->event [instance]
  {:host (get-instance-name instance) :_table "aws.instances"
   :state (get-instance-state instance) :metric (state->metric (get-instance-state instance))
   :public_dns (get-public-dns instance)
   })


;; Stuff that backs directly onto ec2
;;

;; TODO Support injection credentials, and other regions
(defn ec2Client []
  (new AmazonEC2Client (credentials/awsCredentials)))
