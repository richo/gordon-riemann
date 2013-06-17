(ns edda-stream.riemann
  (:require [riemann.client :as r]
            [edda-stream.scala :as s]))

(defonce riemann-client
  (memoize (fn [] (r/tcp-client :host "127.0.0.1"))))

(defn get-instance-state [instance]
  (case (get-in (first (get instance "instances")) ["state" "name"])
        "running" "1"
        "terminated" "0"
        "stopping" "0"
        "stopped" "0"
        "shutting-down" "0"
    nil))


(defn get-instance-name [instance]
  (get (first (get instance "instances")) "instanceId"))

(defn create-event [ev]
  (let [data (s/scala->clj (.data (s/scala->clj ev)))]
    (r/send-event (riemann-client)
                {:service (get-instance-name data) :state (get-instance-state data)})))
