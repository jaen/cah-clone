(ns cah-clone.client.app
  (:require
    [com.palletops.leaven :as leaven :include-macros true]
    ; [cljs.core.async :as async :refer (<! >! put! chan)]
    [com.palletops.bakery.sente :as sente]
    [com.palletops.bakery.om-root :as om-root]
    [cljs.core.match :refer-macros (match)]

    [cah-clone.client.log :as log]
    [cah-clone.client.async :as async]
    [cah-clone.client.ui.main :as ui-main]))

(leaven/defsystem App [:async :om-root])

(defn element-by-id [id]
  (. js/document (getElementById id)))

(defn make-app []
  (let [async      (sente/sente {:path "/sente"
                                 :handler (async/make-handler)
                                 :announce-fn #()})
        app-state {}
        om-root   (om-root/om-root ui-main/main-component app-state {:target (element-by-id "ui-main")})]
    (map->App {:async async :om-root om-root})))
