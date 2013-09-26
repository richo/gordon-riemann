(ns gordon.edda.aws.autoscaling)

(defn get-group-name [group]
  group)

(defn group->event [group]
  {:host (get-group-name group) :_table "aws.autoScalingGroups"})
