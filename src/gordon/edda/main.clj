;; CLI entry point

(ns gordon.edda.main
  (:use [gordon.edda.core :only (mainloop)])
  (:import [org.joda.time DateTime]))

(defn -main []
  (mainloop (DateTime/now)))
