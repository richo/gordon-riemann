(ns gordon.test.riemann.core
  (:use [gordon.riemann.core :as c]
        [clojure.test]))

; Create a handler that atomically bumps a value
(defn bump-value
  [value]
  (fn [_]
    (swap! value (fn [old] (+ old 1)))))

(let [test-value (atom 0)]
  (c/add-handler (bump-value test-value))
  (c/add-handler (bump-value test-value))
  (c/add-handler (bump-value test-value))

  (deftest runs-all-handlers
    (c/handle-event nil)
    (is (= @test-value
           3))))
