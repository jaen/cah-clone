(ns cah-clone.client.logic
  (:require
    [com.palletops.leaven :as leaven :include-macros true]
    [com.palletops.leaven.protocols :as leaven-protocols]
    [cljs.core.async :as async :refer (<! >! put! alts! close! timeout chan)]

    [cah-clone.client.log :as log])
  (:require-macros [cljs.core.async.macros :as m :refer [go go-loop]]))

(defn send-off-request [send-fn req]
  (let [{:keys [:event :payload :callback]} req]
    (send-fn [event payload] 4000 callback)))

(defn set-up-req-forward! [from sente-socket]
  (let [{:keys [:send-fn]} sente-socket]
    (go
      (while true
        (send-off-request send-fn (<! from))))))

(defn initial-state [async]
  {:number 1})

(defn shared-state [async]
  (let [req-chan        (chan)
        shared          {:req-chan req-chan}]
    (set-up-req-forward! req-chan (:channel-socket async))
    shared))

(defrecord Logic [async state]
  leaven-protocols/Startable
  (start [this]
    (log/log "Setting up initial state.")
    (let [initial-state (initial-state async)
          shared-state  (shared-state async)]
      (do
        (log/log "Initial state: %s" initial-state)
        (assoc this :state initial-state :shared-state shared-state))))

  leaven-protocols/Stoppable
  (stop [this]
    this))

(defn make-logic [{:keys [:async] :as options}]
  (map->Logic {:async async}))
