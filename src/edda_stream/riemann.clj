(ns edda-stream.riemann
  (:require [riemann.client :as r]))

(defonce riemann-client
  (memoize (fn [] (r/tcp-client :host "127.0.0.1"))))

(defn create-event [ev]
  )
