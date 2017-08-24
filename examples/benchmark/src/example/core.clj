(ns example.core
  (:gen-class)
  (:require [criterium.core :as criterium]
            [hiccup2.core :as hiccup]))

;; load order is important thing just in this test
(require 'example.naive)
(require 'example.macro)
(require 'example.function)


(def opts
  {:values {:email "foobar" :password "p@sswOad"}
   :errors {:email "please input your email address"}})

(defn execute-naive-version []
  (example.naive/naive-form opts))

(defn execute-macro-version []
  (example.macro/macro-form opts))

(defn execute-function-version []
  (example.function/function-form opts))

(defn same-result? []
  ;; should be true
  (println (= (str (hiccup/html (execute-naive-version)))
              (str (hiccup/html (execute-macro-version)))
              (str (hiccup/html (execute-function-version))))))

(defn -main [& args]
  (same-result?)
  (println "naive version")
  (criterium/bench (execute-naive-version))
  (println "macro version")
  (criterium/bench (execute-macro-version))
  (println "function version")
  (criterium/bench (execute-function-version)))
