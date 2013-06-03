(defproject edda-stream "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.5.0"]
                 [com.novemberain/monger "1.5.0"]
                 [com.amazonaws/aws-java-sdk "1.3.11" :exclusions [org.codehaus.jackson/jackson-mapper-asl]]])
