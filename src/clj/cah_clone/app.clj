(ns cah-clone.app
  "CAH Clone app system"
  (:require
    [com.palletops.bakery.sente :as sente]
    [com.palletops.leaven :as leaven]
    [com.stuartsierra.component :as component]

    [cah-clone.server        :as server]
    [cah-clone.logic         :as logic]
    [cah-clone.server.state  :as state]
    [cah-clone.server.router :as router]
    [cah-clone.server.nrepl  :as nrepl]
    [cah-clone.async         :as async]))

(defn make-app [options]
  (let [server-port (:port options 300)
        server      (server/make-server {:port server-port :env :dev})]
    (component/system-map
      :nrepl  (nrepl/make-nrepl)
      :async  (async/make-async)
      :router (component/using (router/make-router) [:async])
      :server (component/using server [:router]))))
