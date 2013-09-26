(ns gordon.riemann.aws.credentials
  (:import [com.amazonaws.auth BasicAWSCredentials]))

;; TODO also check AWS_CREDENTIAL_FILE
(defn credentials []
  {:accessKey (or (System/getenv "AWS_ACCESS_KEY") (throw (Exception. "No AWS_ACCESS_KEY")))
   :secretKey (or (System/getenv "AWS_SECRET_KEY") (throw (Exception. "No AWS_SECRET_KEY")))})
;;
;; Returns an AWSCredentials object, usable by the java API
(defn awsCredentials []
  (let [c (credentials)]
    (new BasicAWSCredentials (:accessKey c) (:secretKey c))))

