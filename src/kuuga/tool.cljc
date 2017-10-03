(ns kuuga.tool
  (:require [clojure.string :as str]))

(defn- maybe-parse-css-classes [classes]
  (when classes (str/split classes #"\.")))

(def ^:private re-tag #"([^\s\.#]+)(?:#([^\s\.#]+))?(?:\.([^\s#]+))?")

(defn parse-tag-name [tag-name]
  {:pre [(or (string? tag-name) (keyword? tag-name) (symbol? tag-name) (nil? tag-name))]}
  (when-let [parsed (some->> (cond-> tag-name
                               (or (keyword? tag-name) (symbol? tag-name)) name)
                             (re-matches re-tag))]
    (some-> parsed (update-in [3] maybe-parse-css-classes))))

(defn parse-tag-vector [tag-vector]
  {:pre [(vector? tag-vector)]}
  (let [[tag-name & contents] tag-vector
        tag-options (when (map? (first contents)) (first contents))
        contents (cond-> contents (map? tag-options) next)]
    [tag-name tag-options contents]))
