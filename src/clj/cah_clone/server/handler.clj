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
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
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

(defn not-found-handler [request]
  (let [message (str "Page not found: " (:uri request))]
    (timbre/info message)
    (ring-response/not-found message)))

(def handlers
  {:index      index-handler
   :assets     public-resource-handler
   :not-found  not-found-handler})

(defn find-handler [by]
  (cond
    (keyword?  by) (handlers by)
    :else      by))

(defn make-handler [routes {:keys [:env :additional-handlers] :as options}]
  (let [reload-in-dev #(if (= env :dev) (reload/wrap-reload %) %)
        handler       (->
                        (bidi-ring/make-handler routes find-handler)
                        wrap-keyword-params
                        wrap-params
                        wrap-stacktrace
                        wrap-absolute-redirects
                        wrap-not-modified
                        wrap-stacktrace
                        (wrap-anti-forgery {:read-token (fn [req] (-> req :params :csrf-token))})
                        wrap-session
                        reload-in-dev)
                        ; (logger/wrap-with-logger
                        ;   :info  #(timbre/info %)
                        ;   :debug #(timbre/debug %)
                        ;   :error #(timbre/error %)
                        ;   :warn  #(timbre/warn %)))
        ]
    handler))
