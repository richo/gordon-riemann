(ns gordon.riemann.core
  )

(def handlers (atom []))

(defn set-handlers [handlers]
  (swap! handlers (fn [old] handlers)))

(defn reset-handlers []
  (swap! handlers (fn [old] [])))

(defn add-handler [handler]
  (swap! handlers conj handler))

(defn handle-event [event]
  (map (fn [hndlr] (hndlr event))
       (deref handlers)))
