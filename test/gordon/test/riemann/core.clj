(ns gordon.test.riemann.core
  (:use [gordon.riemann.core :as c]
        [clojure.test]))

; Create a handler that atomically bumps a value
(defn bump-value
  [value]
  (fn [_]
    (swap! value (fn [old] (+ old 1)))))

(let [test-value (atom 0)]
  (deftest runs-all-handlers
    (c/reset-handlers)
    (c/add-handler (bump-value test-value))
    (c/add-handler (bump-value test-value))
    (c/add-handler (bump-value test-value))
    (c/handle-event nil)
    (is (= @test-value
           3))))


(defn one-handler [ev])
(defn another-handler [ev])

(let [test-value (atom 0)]
  (deftest removes-handlers
    (c/reset-handlers)
    (c/add-handler one-handler)
    (c/add-handler another-handler)
    (c/remove-handler one-handler)
    (is (= (c/get-handlers) [another-handler]))))
