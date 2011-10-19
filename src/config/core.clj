(ns config.core)

(defn bind-configuration [configuration function]
  (if (var? configuration)
    (fn [& args]
      (apply function (var-get configuration) args))
    (partial function configuration)))

(defn config
  ([configuration functions]
     (into {}
           (map #(let [[symbol function] %
                       function (bind-configuration configuration function)]
                   [symbol function]) functions))))

(defn do-config
  ([ns config]
     (dorun
      (map #(apply intern ns %) config)))
  ([config]
     (do-config *ns* config)))
