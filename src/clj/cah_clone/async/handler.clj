(ns cah-clone.async.handler
  (:require [taoensso.timbre :as timbre]
            [taoensso.sente  :as sente]
            [clojure.core.async :as async :refer (<! <!! >! >!! put! chan go go-loop)]
            [ring.middleware.reload :as reload]
            [ns-tracker.core :as ns-tracker]))

(defmulti async-handler :id) ; Dispatch on event-id

(defn- base-handler [{:as ev-msg :keys [id ?data event]}]
  (timbre/info "Event:" event)
  (async-handler ev-msg))

;; Wrap for logging, catching, etc.:
(defn make-handler []
  (->
    base-handler
    reload/wrap-reload))

(defmethod async-handler :default ; Fallback
  [{:as ev-msg :keys [event id ?data ring-req ?reply-fn send-fn]}]
  (let [session (:session ring-req)
        uid     (:uid     session)]
    (timbre/info "Unhandled event:" event)
    (when ?reply-fn
      (?reply-fn {:umatched-event-as-echoed-from-from-server event}))))
