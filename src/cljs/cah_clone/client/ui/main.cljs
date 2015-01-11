(ns cah-clone.client.ui.main
  (:require-macros
     [cljs.core.async.macros :refer [go]])
    (:require
     [cljs.core.async :refer [put! chan <! >!]]
     [clojure.data :as data]
     [clojure.string :as string]
     [om.core :as om :include-macros true]
      [om.dom :as dom :include-macros true]
     [sablono.core :as html :refer-macros [html]]

     [cah-clone.client.log :as log]))

(def number-atom (atom 1))

(defn make-testfn [channel state]
  (fn [e]
    (go
      (log/log "test")
      (>! channel {:event   :cah-clone.test/test
                   :payload {:number (:number state)}
                   :callback (fn [resp]
                               (om/transact! state :number (fn [_] (:new-number resp)))
                               (log/log "Response to test: %s" resp))}))))

(defn user-component [state owner]
  (om/component
    (html [:li.login.has-form
            [:form.row.collapse
              [:div.large-5.columns
                [:input {:type "text" :placeholder "username"}]]
              [:div.large-5.columns
                [:input {:type "password" :placeholder "password"}]]

              [:div.large-2.columns
                [:input {:type "submit" :value "Login" :class "button expand"}]]]])))

(defn top-bar-component [state owner]
  (om/component
    (html [:nav.top-bar
            [:ul.title-area
              [:li.name
                [:h1 (html/link-to "/" "cah-clone")]]]
            [:ul.right
              (om/build user-component {})]])))

(defn body-component [state owner]
  (om/component
    (let [shareds (om/get-shared owner)
          channel (:req-chan shareds)]
      (html [:div.body [:p "Hello World"]
                       [:button.test {:on-click (make-testfn channel state)} (str "React!" (:number state))]]))))

(defn main-component [state owner]
  (reify
    om/IRender
    (render [this]
      (html [:div (om/build top-bar-component state)
                  (om/build body-component state)]))))
