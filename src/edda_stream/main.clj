;; CLI entry point

(ns edda-stream.main
  (:use [edda-stream.core :only (mainloop)])
  (:import [org.joda.time DateTime]))

(defn -main []
  (mainloop (DateTime/now)))
