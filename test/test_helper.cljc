(ns test-helper
  (:require [kuuga.growing :as k.growing]))

(defn reset-multifn []
  (remove-all-methods k.growing/transform-by-tag)
  (remove-all-methods k.growing/transform-by-id)
  (remove-all-methods k.growing/transform-by-class)

  (defmethod k.growing/transform-by-tag :default
    [_ options tag-vector] tag-vector)

  (defmethod k.growing/transform-by-id :default
    [_ options tag-vector] tag-vector)

  (defmethod k.growing/transform-by-class :default
    [_ options tag-vector] tag-vector))
