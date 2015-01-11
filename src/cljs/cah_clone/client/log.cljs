(ns cah-clone.client.log
  (:require
    [taoensso.encore :as encore :refer (logf)]))

(defn log [fmt &xs]
  (apply encore/logf (concat [fmt] xs)))
