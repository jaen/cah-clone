(ns cah-clone.routes)

(defn combined-routes
  ([]     (combined-routes []))
  ([with] (concat [[""                                  :index]]
                  with
                  [[["assets" [#".*" :resource-path]] :assets]
                   [#".*" :not-found]])))

(defn make-routes
  ([] (make-routes {}))
  ([{:keys [:additional-routes] :as options}]
    (let [routes (apply combined-routes additional-routes)]
      ["/" routes])))
