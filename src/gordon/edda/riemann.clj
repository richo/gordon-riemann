(ns gordon.edda.riemann
  (:require [riemann.client :as r]
            [gordon.edda.scala :as s]
            [gordon.edda.aws.ec2 :as ec2]
            [gordon.edda.aws.route53 :as route53]))

(defonce riemann-client
  (memoize (fn [] (r/tcp-client :host "127.0.0.1"))))

(defn event-factory [typ]
  (case typ
    "aws.instances" ec2/instance->event
    "aws.hostedRecords" route53/hostedRecord->event
    ))

(defn base-event [ev]
  {:_id (s/record->_id ev)
   :_service "gordon-riemann"})

(defn create-event [table ev]
  (let [data (s/scala->clj (.data (s/scala->clj ev)))]
    (r/send-event (riemann-client)
                  (into (base-event ev) ((event-factory table) data)))))
