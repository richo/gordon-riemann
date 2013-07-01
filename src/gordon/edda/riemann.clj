(ns gordon.edda.riemann
  (:require [riemann.client :as r]
            [gordon.edda.scala :as s]))

(defonce riemann-client
  (memoize (fn [] (r/tcp-client :host "127.0.0.1"))))

(defn instance->event [instance]
  {:host (get-instance-name data) :service (get-instance-role data)
   :state (get-instance-state data) :metric (state->metric (get-instance-state data))})

(defn get-instance-state [instance]
  (get-in (first (get instance "instances")) ["state" "name"]))

(defn get-instance-name [instance]
  (get (first (get instance "instances")) "instanceId"))

; TODO
(defn get-instance-role [instance]
  (get (first (get instance "instances")) "instanceId"))


(defn state->metric [state]
  (case state
        "running" 0
        "terminated" 1
        "stopping" 2
        "stopped" 4
        "shutting-down" 8))

(defn create-event [ev]
  (let [data (s/scala->clj (.data (s/scala->clj ev)))]
    (r/send-event (riemann-client)
                  (instance->event data))))
