(ns cah-clone.server.handler
  (:require [ring.util.response :as ring-response]
            [bidi.ring :as bidi-ring]
            [taoensso.timbre :as timbre]
            [ring.middleware.absolute-redirects :refer [wrap-absolute-redirects]]
            [ring.middleware.anti-forgery :refer [wrap-anti-forgery]]
            [ring.middleware.not-modified :refer [wrap-not-modified]]
            [ring.middleware.session :refer [wrap-session]]
            [ring.middleware.stacktrace :refer [wrap-stacktrace]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            ; [ring.middleware.logger :as logger]
            [ring.middleware.reload :as reload]
            [ns-tracker.core :as ns-tracker]))

(defn index-handler [request]
  (timbre/info "Loading index.")
  (assoc-in (ring-response/resource-response "index.html" {:root "public"}) [:headers "Content-Type"] "text/html"))

(defn public-resource-handler' [request]
  (let [resource-path (get-in request [:params :resource-path])]
    (timbre/info "Loading resource: " resource-path)
    (ring-response/resource-response resource-path {:root "public"})))

(def public-resource-handler
  (wrap-content-type public-resource-handler'))

(def handlers
  {:index  index-handler
   :assets public-resource-handler})

(defn make-handler [routes {:keys [:env] :as options}]
  (let [reload-in-dev #(if (= env :dev) (reload/wrap-reload %) %)
        handler       (->
                        (bidi-ring/make-handler routes #'handlers)
                        reload-in-dev
                        wrap-stacktrace
                        wrap-absolute-redirects
                        wrap-not-modified
                        wrap-stacktrace
                        (wrap-anti-forgery {:read-token (fn [req] (-> req :params :csrf-token))})
                        wrap-session)
                        ; (logger/wrap-with-logger
                        ;   :info  #(timbre/info %)
                        ;   :debug #(timbre/debug %)
                        ;   :error #(timbre/error %)
                        ;   :warn  #(timbre/warn %)))
        ]
    handler))
