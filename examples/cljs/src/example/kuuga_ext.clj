(ns example.kuuga-ext
  (:require [kuuga.growing :as growing]
            [kuuga.tool :as tool]))

(defmethod growing/transform-by-class :form-group
  [_ options tag-vector]
  (let [[tagname tagopts contents] (tool/parse-tag-vector tag-vector)
        contents (reduce (fn [contents' tagvec']
                           (let [[tn to _] (tool/parse-tag-vector tagvec')
                                 [_ t] (tool/parse-tag-name tn)]
                             (cond-> (conj contents' tagvec')
                               (= t "input") (conj `(example.core/invalid-feedback ~options ~to)))))
                         []
                         contents)]
    `[~tagname
      ~tagopts
      ~@contents]))

(defmethod growing/transform-by-tag :input
  [_ options tag-vector]
  (let [[tagname tagopts contents] (tool/parse-tag-vector tag-vector)]
    `[~tagname
      (example.core/update-input-opts ~options ~tagopts)
      ~@contents]))
