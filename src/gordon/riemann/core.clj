(ns gordon.riemann.core
  )

(def handlers (atom []))

(defn get-handlers []
  (deref handlers))

(defn set-handlers [handlers]
  (swap! handlers (fn [old] handlers)))

(defn reset-handlers []
  (swap! handlers (fn [old] [])))

(defn add-handler [handler]
  (swap! handlers conj handler))

(defn handle-event [event]
  (doseq [hndlr (deref handlers)]
    (hndlr event)))
