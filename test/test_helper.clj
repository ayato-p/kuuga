(ns test-helper
  (:require [kuuga.growing :as k.growing]))

(defn reset-multifn []
  (println "reset multifn")
  (remove-all-methods k.growing/transform-by-tag)
  (remove-all-methods k.growing/transform-by-id)
  (remove-all-methods k.growing/transform-by-class)
  (load "/kuuga/growing"))
