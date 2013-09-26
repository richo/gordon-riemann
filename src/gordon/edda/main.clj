;; CLI entry point

(ns gordon.edda.main
  (:require [clojure.tools.logging :as log])
  (:use [gordon.edda.core :only (mainloop)]
        [gordon.riemann.provisioner.ssh :as ssh]
        )
  (:import [org.joda.time DateTime]))

(defn -main []
  (log/warn "gordon is initialized")
  (mainloop ["aws.instances" "aws.hostedRecords"] (DateTime/now)))
