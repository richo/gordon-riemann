(ns gordon.riemann.provisioner.ssh
  (:require [clj-ssh.ssh :as ssh]
            [clojure.tools.logging :as log]
            [clojure.java.io :as io]))

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
    (let [ret (ssh/ssh host {:cmd cmd :out :stream :agent-forwarding true})]
      (io/copy (:out-stream ret) (log/log-stream :info (.getHost host)))
      (.getExitStatus (:channel ret)))))

(def github-host-key
  (execute-and-return "ssh-keyscan github.com >> .ssh/known_hosts"))

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
  (run-provisioners (ssh-to host) [babushka github-host-key provisioner]))
