(ns edda-stream.scala
  (import [scala Predef]
          [scala.collection JavaConverters JavaConversions]))

(defprotocol Scalable
  "Convert various Clojure data structures to immutable Scala equivalents."
  (clj->scala [this]))

(extend-protocol Scalable

  clojure.lang.IPersistentMap
  (clj->scala [m]
    (-> (JavaConverters/mapAsScalaMapConverter m)
        (.asScala)
        (.toMap (Predef/conforms))))

  clojure.lang.IPersistentSet
  (clj->scala [s]
    (-> (JavaConverters/asScalaSetConverter s)
        (.asScala)
        (.toSet))))

(defprotocol Clojureable
  "Convert Scala things into Clojure things."
  (scala->clj [this]))

(extend-protocol Clojureable

  scala.collection.Iterable
  (scala->clj [coll]
    (JavaConversions/asJavaIterable coll)))
