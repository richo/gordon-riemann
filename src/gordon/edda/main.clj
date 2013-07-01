;; CLI entry point

(ns gordon.edda.main
  (:require [clojure.tools.logging :as log])
  (:use [gordon.edda.core :only (mainloop)])
  (:import [org.joda.time DateTime]))

(defn -main []
  (log/warn "gordon is initialized")
  (mainloop (DateTime/now)))
