(ns gordon.riemann.aws.credentials
  (:use [clojure.java.io]
        [clojure.string :only (split)])
  (:import [com.amazonaws.auth BasicAWSCredentials]))

(defn- parse-credential-file [filename]
  (with-open [rdr (reader filename)]
    (into {} (map (fn [line]
                    (let [[k v] (split line #"=" 2)]
                      (case k
                        "AWSAccessKeyId" {:accessKey v}
                        "AWSSecretKey" {:secretKey v}
                        )))
                  (line-seq rdr)))))

(defn- credentials-from-credential-file []
  (let [location (System/getenv "AWS_CREDENTIAL_FILE")]
    (if location
      (parse-credential-file location))))

(defn- credentials-from-environment []
  (let [accesskey (System/getenv "AWS_ACCESS_KEY")
        secretkey (System/getenv "AWS_SECRET_KEY")]
    (if (and accesskey secretkey)
      {:accessKey accesskey
       :secretKey secretkey})))

(defn credentials []
  (or (or (credentials-from-credential-file) (credentials-from-environment))
      (throw (Exception. "No credentials available"))))

;;
;; Returns an AWSCredentials object, usable by the java API
(defn awsCredentials []
  (let [c (credentials)]
    (new BasicAWSCredentials (:accessKey c) (:secretKey c))))
