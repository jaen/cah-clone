(ns cah-clone.core
  (:gen-class)
  (:require  [clojure.tools.cli :as cli]
             [com.palletops.leaven :as leaven]
             [clojure.tools.namespace.repl :as repl]
             [com.stuartsierra.component :as component]

             [cah-clone.app :as app]))

(def cli-options
  ;; An option with a required argument
  [["-p" "--port PORT" "Port number"
    :default  3000
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]])

(def app (atom nil))

(defn init-app
  ([]        (init-app {:env :dev :port 3000}))
  ([options] (reset! app (app/make-app options))))

(defn start-app []
  (swap! app #(when % (component/start %))))

(defn stop-app []
  (swap! app #(component/stop %)))

(defn go []
  (init-app)
  (start-app))

(defn reset []
  (stop-app)
  (repl/refresh :after 'cah-clone.core/go))

(defn -main [& args]
  (let [{:keys [options errors]} (cli/parse-opts args cli-options)
         additional-options      {:env :dev}
         effective-options       (merge additional-options options)]
    (init-app effective-options)
    (start-app)))
