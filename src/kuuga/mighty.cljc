(ns kuuga.mighty
  (:require [clojure.walk :as walk]
            [kuuga.growing :as k.growing]))

(defn transform*
  ([hiccup-data] (transform* nil hiccup-data))
  ([options hiccup-data]
   (k.growing/reset-context!)
   (let [f (partial k.growing/modifier options)]
     (walk/prewalk f hiccup-data))))

(defn transform [options & hiccup-data]
  (transform* options hiccup-data))
