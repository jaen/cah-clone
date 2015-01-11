(ns cah-clone.client.om-root
  (:require
    [com.palletops.leaven :as leaven :include-macros true]
    [com.palletops.leaven.protocols :as leaven-protocols]
    [om.core :as om :include-macros true]

    [cah-clone.client.log :as log]))

(defrecord OmRoot [component logic options]
  leaven-protocols/Startable
  (start [this]
    (let [state             (:state logic)
          shared-state      (:shared-state logic)
          effective-options (merge options {:shared shared-state})]
      (log/log "Setting up om root with state: %s" state)
      (log/log "Setting up om root with shared-state: %s" shared-state)
      (om/root component state effective-options))
      this)

  leaven-protocols/Stoppable
  (stop [this]
    (om/detach-root (:target options))
    this))


(defn make-om-root [{:keys [:component :logic :options] :as options}]
  (map->OmRoot {:component component :logic logic :options options}))
