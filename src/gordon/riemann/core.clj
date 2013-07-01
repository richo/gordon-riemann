(ns gordon.riemann.core
  )

(def handlers (atom []))

(defn add-handler [handler]
  (swap! handlers conj handler))

(defn handle-event [event]
  (map (fn [hndlr] (hndlr event))
       (deref handlers)))
