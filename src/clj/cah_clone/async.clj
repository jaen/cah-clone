(ns cah-clone.async
  (:require [bidi.bidi :as bidi]
            [com.stuartsierra.component :as component]
            [taoensso.timbre :as timbre]
            [taoensso.sente  :as sente]
            [clojure.core.async :as async :refer (<! <!! >! >!! put! chan go go-loop)]

            [cah-clone.async.handler :as async-handler]))

(defn make-sente-routes [sente-socket]
  (let [ajax-post-fn    (:ajax-post-fn sente-socket)
        handshake-fn    (:ajax-get-or-ws-handshake-fn sente-socket)]
    [[:get  [["sente" handshake-fn]]]
     [:post [["sente" ajax-post-fn]]]]))

(defrecord Async [env]
  component/Lifecycle
  (start
   [component]
   (if (:sente component)
      component
      (do
        (timbre/info "Starting async.")
        (let [sente-socket    (sente/make-channel-socket! {})
              receive-chan    (:ch-recv sente-socket)
              sente-send!     (:send-fn sente-socket)
              connected-uids  (:connected-uids sente-socket)
              sente-routes    (make-sente-routes sente-socket)
              sente-router    (sente/start-chsk-router! receive-chan (async-handler/make-handler))]
          (timbre/info "Started async.")
          (assoc component :sente sente-socket :routes sente-routes :router sente-router :receive-chan receive-chan :send! sente-send! :uids connected-uids)))))
  (stop
    [component]
    (if-let [sente-router-stop! (:router component)]
      (do
        (timbre/info "Stopping async.")
        (sente-router-stop!)
        (dissoc component :sente :routes :router :receive-chan :send! :uids))
      component)))

(defn make-async
  "Creates an async component.
  Key :ring-routes should be used by an http-kit server."
  []
  (map->Async {:env :dev}))
