(ns cah-clone.server.router
  (:require [bidi.bidi :as bidi]
            [com.stuartsierra.component :as component]
            [taoensso.timbre :as timbre]

            [cah-clone.routes :as routes]
            [cah-clone.server.handler :as handler]))

(defrecord Router [env async]
  component/Lifecycle
  (start
   [component]
   (let [async-routes (:routes async)
         routes       (routes/make-routes {:additional-routes [async-routes]}); (bidi/compile-route (routes/make-routes))
         handler      (handler/make-handler routes { :env env })]
        (timbre/info "Created router for routes:" routes)
        (assoc component :routes routes :handler handler)))
  (stop
   [component]
   (dissoc component :routes :handler)))

(defn make-router
  "Creates a router component.
  Key :ring-routes should be used by an http-kit server."
  []
  (map->Router {:env :dev}))
