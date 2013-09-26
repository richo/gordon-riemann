(ns gordon.riemann.provisioner.ssh
  (:require [clj-ssh.ssh :as ssh]))

(def ssh-agent
  (ssh/ssh-agent {}))

(def strict-host-key-checking
  :no)

(def debug
  1)

(defn ssh-to [host]
  ;; Returns an ssh container to host
  (ssh/session ssh-agent host {:strict-host-key-checking strict-host-key-checking
                               :username "ubuntu"}))

(defn execute-and-return [cmd]
  (fn [host]
    (let [ret (ssh/ssh host {:in cmd :agent-forwarding true})]
      (if debug (println (:out ret)))
      (:exit ret))))


(def babushka
  (execute-and-return "wget -O - https://babushka.me/up | sudo bash"))

(defn- run-provisioners [host provisioners]
  (let [provisioner (first provisioners)]
    (if (nil? provisioner) 0
      (let [ret (provisioner host)]
        (if (= 0 ret)
          (recur host (rest provisioners))
          ret)))))

(defn provision [host provisioner]
  (run-provisioners (ssh-to host) [babushka provisioner]))
