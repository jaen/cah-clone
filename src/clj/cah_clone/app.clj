(ns cah-clone.app
  "CAH Clone app system"
  (:require
    [com.palletops.bakery.sente :as sente]
    [com.palletops.leaven :as leaven]
    [com.stuartsierra.component :as component]

    [cah-clone.server        :as server]
    [cah-clone.server.state  :as state]
    [cah-clone.server.async  :as async]
    [cah-clone.server.router :as router]))

(defn make-app [options]
  (let [server-port (:port options 300)
        server      (server/make-server {:port server-port :env :dev})]
    (component/system-map
      :router (router/make-router)
      :server (component/using server [:router]))))
