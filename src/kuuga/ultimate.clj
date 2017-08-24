(ns kuuga.ultimate
  (:require [kuuga.growing :as k.growing]
            [clojure.walk :as walk]))

(defmacro transform*
  ([hiccup-data]
   `(transform* {} ~hiccup-data))
  ([options hiccup-data]
   (k.growing/reset-context!)
   (let [f (partial k.growing/modifier options)
         transformed (walk/prewalk f hiccup-data)]
     (if (vector? transformed)
       transformed
       `(list ~@transformed)))))

(defmacro transform [options & hiccup-data]
  `(transform* ~options ~hiccup-data))
