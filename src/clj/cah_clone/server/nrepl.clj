(ns cah-clone.server.nrepl
  (:require [com.stuartsierra.component :as component]
            [taoensso.timbre :as timbre]
            [clojure.tools.nrepl.server :as nrepl]))

(defrecord NRepl [env]
  component/Lifecycle
  (start
    [component]
    (timbre/info "Starting nRepl at port 7888.")
    (let [server (nrepl/start-server :port 7888)]
      (assoc component :nrepl server)))
  (stop
    [component]
    (timbre/info "Stopping nRepl.")
    (nrepl/stop-server (:nrepl component))
    (dissoc component :nrepl)))

(defn make-nrepl
  "Creates a router component.
  Key :ring-routes should be used by an http-kit server."
  []
  (map->NRepl { :env :dev }))
