(ns cah-clone.client.async
  (:require
    [cah-clone.client.log :as log]))

; (ns cah-clone.async.handler
;   (:require [clojure.core.async :as async :refer (<! <!! >! >!! put! chan go go-loop)]
;             [ring.middleware.reload :as reload]
;             [ns-tracker.core :as ns-tracker]))

(defmulti async-handler :id)

(defn- base-handler [{:as ev-msg :keys [id ?data event]}]
  (log/log "Event:" event)
  (async-handler ev-msg))

(defn make-handler []
  base-handler)

(defmethod async-handler :default ; Fallback
  [{:as ev-msg :keys [event id ?data ring-req ?reply-fn send-fn]}]
  (let [session (:session ring-req)
        uid     (:uid     session)]
    (log/log "Unhandled event:" event)
    (when ?reply-fn
      (?reply-fn {:test event}))))

; (defn handle-receive [state]
;   (fn [{:keys [event send-fn]}]
;     (let [[id data :as ev] event]
;       (log/log "Event:" event)
;       (log/log "Event id:" id )
;       (log/log "Event:" ev)
;       (match [id data]
;         [:chsk/state {:first-open? true}]
;         (log/log "Channel socket successfully established!")

;         [:chsk/state new-state]
;         (log/log "Chsk state change: %s" new-state)

;         [:chsk/recv payload]
;         (log/log "Push event from server: %s" payload)

;         :else (log/log "Unmatched event: %s" ev)))))
