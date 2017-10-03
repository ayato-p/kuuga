(ns kuuga.growing
  (:require [kuuga.tool :as k.tool]))

(declare transform-by-tag transform-by-id transform-by-class)

(defonce +transform-context+ (atom {}))

(defn reset-context! []
  (reset! +transform-context+ {}))

(defn- tag-vector? [tag-vector]
  (and (vector? tag-vector)
       (let [tag-name (first tag-vector)]
         (or (keyword? tag-name)
             (string? tag-name)
             (symbol? tag-name)))))

(defn modifier [options tag-vector]
  (if (tag-vector? tag-vector)
    (let [[tag-name & _] tag-vector
          [origin tag id classes] (k.tool/parse-tag-name tag-name)]
      (reduce (fn [tagvec clazz]
                (transform-by-class clazz options tagvec))
              (->> tag-vector
                   (transform-by-tag (keyword tag) options)
                   (transform-by-id (keyword id) options))
              (map keyword classes)))
    tag-vector))

(defmulti transform-by-tag
  (fn [tag options tag-vector] tag))

(defmethod transform-by-tag :default
  [_ options tag-vector] tag-vector)

(defmulti transform-by-id
  (fn [id options tag-vector] id))

(defmethod transform-by-id :default
  [_ options tag-vector] tag-vector)

(defmulti transform-by-class
  (fn [clazz options tag-vector] clazz))

(defmethod transform-by-class :default
  [_ options tag-vector] tag-vector)
