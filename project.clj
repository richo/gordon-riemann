(defproject gordon-riemann "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.5.0"]
                 [org.scala-lang/scala-library "2.10.1"]
                 [riemann-clojure-client "0.2.6"]
                 ;; Logging
                 [org.clojure/tools.logging "0.2.6"]
                 [org.slf4j/slf4j-api "1.6.6"]
                 [org.slf4j/slf4j-log4j12 "1.7.5"]
                 [clj-ssh "0.5.6"]
                 [log4j/log4j "1.2.17"]]
  :source-paths ["resources" ; This must be first because Reasons.
                 "lib/*"
                 "src"]
  :main gordon.edda.main)
