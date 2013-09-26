(ns gordon.riemann.roles.example-instance
  (:import [com.amazonaws.services.ec2.model RunInstancesRequest])
  (:use [gordon.riemann.provisioner.ssh :as ssh]))

(def ssh-provisioner
  ;; TODO IAM credentials, S3 etc.
  (ssh/execute-and-return "hostname")) ; FIXME Actually bootstrap system

(def ami "") ; FIXME AMI
(def instance-type "c1.xlarge")
(def key-name "") ; FIXME Keyname
(def security-groups []) ; FIXME Security Groups

;; Returns a RunInstancesResult which describes the reservation,
;; but not the running instance
(defn provision-ec2-instance [client options]
  (let [run-request
    (-> (new RunInstancesRequest)
      (.withImageId (or (get options :ami) ami))
      (.withInstanceType (or (get options :instanceType) instance-type))
      (.withMinCount (int 1))
      (.withMaxCount (int 1))
      (.withKeyName (or (get options :keyName) key-name))
      (.withSecurityGroups (or (get options :securityGroups) security-groups)))]
    (.runInstances client run-request)))
