(ns gordon.edda.aws
  (:use [gordon.aws.ec2 :as ec2]
        [gordon.aws.route53 :as r53]
        [gordon.aws.autoscaling :as as]))

(defn provision-and-boostrap [instance-provisioner instance-bootstrapper]
  (let [client (ec2/ec2Client)
        reserv (instance-provisioner client {})
        reserv-id (-> reserv .getReservation .getReservationId)
        ;; bind our handler to a name so it's unbindable when done
        bootstrap-fn (fn [ev]
          (if (= (:_id ev) reserv-id)
            (do
              (remove-handler bootstrap-fn)
              (instance-bootstrapper (:public_dns ev)))
            nil
            ))]
    (add-handler bootstrap-fn)))
