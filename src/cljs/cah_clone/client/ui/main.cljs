(ns cah-clone.client.ui.main
  (:require-macros
     [cljs.core.async.macros :refer [go]])
    (:require
     [cljs.core.async :refer [put! chan <!]]
     [clojure.data :as data]
     [clojure.string :as string]
     [om.core :as om :include-macros true]
     [sablono.core :as sablono :include-macros true]))

(defn main-component [state owner]
  (reify
    om/IRender
    (render [this]
      (sablono/html [:div "Hello World"]))))
