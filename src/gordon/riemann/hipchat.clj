(ns gordon.riemann.hipchat
  (:use [gordon.riemann.config :as config])
  (:require [clipchat.rooms]))

(defn- -messager [auth-token room]
  (fn [msg]
    (clipchat.rooms/message auth-token {
      :room_id room
      :message msg
      :from "gordon-riemann"
      :notify true
    })))

(defn messager []
  "Setup a messager for the configured room"
  (-messager config/hipchat-key config/hipchat-room))
