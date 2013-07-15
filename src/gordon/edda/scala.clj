(ns gordon.edda.scala
  (import [scala Predef]
          [scala.collection JavaConverters JavaConversions]))

;; Clojure/Java -> Scala

(defmulti clj->scala
  "Convert Clojure things to Scala things."
  class)

(defmethod clj->scala java.util.Map [m]
  (-> (JavaConverters/mapAsScalaMapConverter m)
      (.asScala)
      (.toMap (Predef/conforms))))

(defmethod clj->scala java.util.Set [s]
  (-> (JavaConverters/asScalaSetConverter s)
      (.asScala)
      (.toSet)))

(defmethod clj->scala :default [x]
  x)

;; Scala -> Clojure/Java

(defmulti scala->clj
  "Convert Scala things to Clojure things"
  class)

(defmethod scala->clj scala.collection.Map [m]
  (into {} (map (fn [[k v]]
                  [(scala->clj k)
                   (scala->clj v)])
                (JavaConversions/asJavaMap m))))

(defmethod scala->clj scala.collection.Iterable [coll]
  (map scala->clj (JavaConversions/asJavaIterable coll)))

(defmethod scala->clj :default [x]
  x)

(defn record->_id [rec]
  "Extract the id directly, as the _id field doesn't exist on the Record"
  ; @ see edda/src/main/scala/com/netflix/edda/mongo/MongoDatastore.scala:87
  (clojure.string/join "|" [(.id rec) (.stime rec)]))
