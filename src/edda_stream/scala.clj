(ns edda-stream.scala
  (import [scala Predef]
          [scala.collection JavaConverters JavaConversions]))

;; Clojure/Java -> Scala

(defmulti clj->scala
  "Convert Clojure things to Scala things."
  class
  :default identity)

(defmethod clj->scala java.util.Map [m]
  (-> (JavaConverters/mapAsScalaMapConverter m)
      (.asScala)
      (.toMap (Predef/conforms))))

(defmethod clj->scala java.util.Set [s]
  (-> (JavaConverters/asScalaSetConverter s)
      (.asScala)
      (.toSet)))

;; Scala -> Clojure/Java

(defmulti scala->clj
  "Convert Scala things to Clojure things"
  class)

(defmethod scala->clj scala.collection.Map [m]
  (into {} (map (fn [[k v]]
                  [(if k (scala->clj k))
                   (if v (scala->clj v))])
                (JavaConversions/asJavaMap m))))

(defmethod scala->clj scala.collection.Iterable [coll]
  (map scala->clj (JavaConversions/asJavaIterable coll)))

(defmethod scala->clj java.lang.Object [x]
  x)
