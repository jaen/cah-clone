(ns cah-clone.client.core
  (:require
    [com.palletops.leaven :as leaven]

    [cah-clone.client.app :as app]))

(enable-console-print!)

(def client-app (atom (app/make-app)))

(try
  (swap! client-app leaven/start)
  ;; This is to try and provide useful information in the console if
  ;; an exception occurs on start.
  (catch ExceptionInfo e
    (.log js/console e)
    (if-let [c (ex-cause e)]
      (throw c)
      (throw e))))

;; (swap! ui leaven/stop)
