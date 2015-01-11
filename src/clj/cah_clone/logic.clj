(ns cah-clone.logic
  (:require
    [taoensso.timbre :as timbre]
    [cah-clone.async.handler :as async-handler]))

(defmethod async-handler/async-handler :cah-clone.test/test
  [{:as ev-msg :keys [event id ?data ring-req ?reply-fn send-fn]}]
  (let [session (:session ring-req)
        uid     (:uid     session)
        number  (:number ?data)]
    (timbre/info "Test event:" event)
    (when ?reply-fn
      (?reply-fn {:reply (str "You sent me a number: " number) :new-number (inc number)}))))
