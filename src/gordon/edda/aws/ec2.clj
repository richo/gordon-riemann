(ns gordon.edda.aws.ec2
  (:require [gordon.edda.scala :as s]))

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

(defn state->metric [state]
  (case state
        "running" 0
        "terminated" 1
        "stopping" 2
        "stopped" 4
        "shutting-down" 8))

(defn instance->event [instance]
  {:host (get-instance-name instance) :service "gordon-riemann"
   :state (get-instance-state instance) :metric (state->metric (get-instance-state instance))
   :_id (s/record->_id instance)})