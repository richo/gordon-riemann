(ns gordon.edda.riemann
  (:require [riemann.client :as r]
            [gordon.edda.scala :as s]))

(defonce riemann-client
  (memoize (fn [] (r/tcp-client :host "127.0.0.1"))))

(defn get-instance-state [instance]
  (get-in (first (get instance "instances")) ["state" "name"]))

(defn get-instance-name [instance]
  (get (first (get instance "instances")) "instanceId"))

(defn extract-tags [tags]
  (into {} (map (fn [t]
             [(get t "key") (get t "value")])
       tags)))
(defn get-instance-role [instance]
  (get (extract-tags (get (first (get instance "instances")) "tags"))) "Role")


(defn state->metric [state]
  (case state
        "running" 0
        "terminated" 1
        "stopping" 2
        "stopped" 4
        "shutting-down" 8))

(defn instance->event [instance]
  {:host (get-instance-name instance) :service (get-instance-role instance)
   :state (get-instance-state instance) :metric (state->metric (get-instance-state instance))})

(defn create-event [ev]
  (let [data (s/scala->clj (.data (s/scala->clj ev)))]
    (r/send-event (riemann-client)
                  (instance->event data))))
