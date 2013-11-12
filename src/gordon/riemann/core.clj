(ns gordon.riemann.core
  )

(defonce handlers (atom []))

(defn get-handlers []
  (deref handlers))

(defn set-handlers [handlers]
  (swap! handlers (fn [old] handlers)))

(defn reset-handlers []
  (swap! handlers (fn [old] [])))

(defn add-handler [handler]
  (swap! handlers conj handler))

(defn remove-handler [handler]
  (swap! handlers (fn [handlers to-remove] (remove (fn [h] (= h to-remove)) handlers)) handler))

(defn handle-event [event]
  (doseq [hndlr (deref handlers)]
    (hndlr event)))
