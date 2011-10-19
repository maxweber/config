(ns config.test.functions)

(defn b [configuration path]
  (str (:host configuration) "/" path))
