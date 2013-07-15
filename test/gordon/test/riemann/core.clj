(ns gordon.test.riemann.core
  (:use [gordon.riemann.core :as c]
        [clojure.test]))

; Set handlers to three identity functions
(defn id [value]
  value)

(c/add-handler id)
(c/add-handler id)
(c/add-handler id)

(deftest runs-all-handlers
  (is (= (c/handle-event 1)
      [1 1 1])))
