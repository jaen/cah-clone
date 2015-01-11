(ns cah-clone.client.app
  (:require
    [com.palletops.leaven :as leaven :include-macros true]
    ; [cljs.core.async :as async :refer (<! >! put! chan)]
    [com.palletops.bakery.sente :as sente]
    [taoensso.sente.packers.transit :as sente-transit]
    [cljs.core.match :refer-macros (match)]

    [cah-clone.client.log :as log]
    [cah-clone.client.om-root :as om-root]
    [cah-clone.client.async :as async]
    [cah-clone.client.logic :as logic]
    [cah-clone.client.ui.main :as ui-main]))

(leaven/defsystem App [:async :logic :om-root]
  {:depends
    {:logic   [:async]
     :om-root [:logic]}})

(defn element-by-id [id]
  (. js/document (getElementById id)))

(def sente-packer
  (sente-transit/get-flexi-packer :edn))

(defn make-app []
  (let [async     (sente/sente {:path "/sente"
                                :handler (async/make-handler)
                                :announce-fn #()
                                :config {
                                  :packer sente-packer}})
        logic     (logic/make-logic {:async async})
        om-root   (om-root/make-om-root {:component ui-main/main-component
                                         :logic logic
                                         :options {:target (element-by-id "ui-main")}})]
    (map->App {:async async :logic logic :om-root om-root})))
