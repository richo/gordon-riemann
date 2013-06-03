(defproject edda-stream "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.5.0"]
                 [com.novemberain/monger "1.6.0-beta2"]
                 [com.amazonaws/aws-java-sdk "1.3.11" :exclusions [org.codehaus.jackson/jackson-mapper-asl]]
                 [org.scala-lang/scala-library "2.10.1"]
                 [clj-time "0.5.1"]]
  :source-paths ["edda/build/libs/edda-2.1-SNAPSHOT.jar" "src/"]
  )
