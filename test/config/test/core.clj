(ns config.test.core
  (:use config.core
        [lazytest.describe
         :only [describe it given do-it using testing]]
        [lazytest.expect :only [expect]])
  (:require config.test.functions))

(def host "localhost")

(def configuration {:host host})

(def functions (merge {'a (fn [configuration] (:host configuration))}
                      (ns-interns 'config.test.functions)))

(do-config *ns* (config configuration functions))

(def ^{:dynamic true} *configuration* configuration)

(def more-functions {'c (fn [configuration] (:host configuration))})

(do-config (config #'*configuration* more-functions))

(describe "config"
  (it "should bind the configuration as first argument to the given functions and intern them to the given symbol in the given namespace"
    (= host (a)))
  (it "should support vars inside the functions map"
    (= (str host "/" "search") (b "search")))
  (given [host-value "127.0.0.1"]
    (do-it "should support to rebind the *configuration* variable"
      (expect (= host (c)))
      (expect (= host-value (binding [*configuration* {:host host-value}]
                              (c)))))))
