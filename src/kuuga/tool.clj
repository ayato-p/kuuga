(ns kuuga.tool
  (:require [clojure.string :as str]))

(defn- maybe-parse-css-classes [classes]
  (when classes (str/split classes #"\.")))

(def ^:private re-tag #"([^\s\.#]+)(?:#([^\s\.#]+))?(?:\.([^\s#]+))?")

(defn parse-tag-keyword [tag-keyword]
  {:pre [(or (keyword? tag-keyword) (string? tag-keyword) (nil? tag-keyword))]}
  (when-let [parsed (some->> (cond-> tag-keyword (keyword? tag-keyword) name)
                             (re-matches re-tag))]
    (some-> parsed (update-in [3] maybe-parse-css-classes))))

(defn parse-tag-vector [tag-vector]
  {:pre [(vector? tag-vector)]}
  (let [[tag-keyword & contents] tag-vector
        tag-options (when (map? (first contents)) (first contents))
        contents (cond-> contents (map? tag-options) next)]
    [tag-keyword tag-options contents]))
