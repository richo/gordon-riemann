(ns gordon.edda.aws.autoscaling)

(defn group->event [group]
  {:host (get-group-name group) :_table "aws.autoScalingGroups"})
