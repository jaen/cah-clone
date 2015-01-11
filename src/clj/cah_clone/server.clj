(ns cah-clone.server
  (:require [com.palletops.leaven :as leaven]
            [com.palletops.leaven.protocols :as leaven-protocols]
            [org.httpkit.server :as http-kit]
            [taoensso.timbre :as timbre]
            [com.stuartsierra.component :as component]))

(defrecord Server [port env routes handler server]
  component/Lifecycle
  (start [component]
    (if (:server component)
      component
      (do
        (timbre/info "Starting webserver.")
        (let [handler (get-in component [:router :handler])
              server (http-kit/run-server handler { :port (or port 0) })
              port (-> server meta :local-port)]
          (timbre/info "Web server running on port" port ".")
          (assoc component :server server :port port)))))
  (stop [component]
    (if-let [stop-server! (:server component)]
      (do
        (timbre/info "Stopping webserver.")
        (stop-server! :timeout 250)
        (timbre/info "Stopped webserver.")
        (dissoc component :server :router))
      component)))


(defn make-server
  "Creates a router component."
  ([]         (make-server {}))
  ([options]  (map->Server options)))
