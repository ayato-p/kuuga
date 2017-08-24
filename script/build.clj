(ns build
  (:require [cljs.build.api :as api]
            [kuuga.growing :as k.growing]
            [kuuga.tool :as k.tool]))

(defmethod k.growing/transform-by-tag :input
  [_ opts tagvec]
  (let [[tagkw tagopts contents] (k.tool/parse-tag-vector tagvec)]
    `[~tagkw
      (kuuga.ultimate-cljs-test/update-opts-for-input ~tagopts)
      ~@contents]))

(defmethod k.growing/transform-by-id :kuuga
  [_ opts tagvec]
  (let [[tagkw tagopts contents] (k.tool/parse-tag-vector tagvec)]
    `[~tagkw
      (kuuga.ultimate-cljs-test/update-opts-for-kuuga ~tagopts ~tagkw)
      ~@contents]))

(api/build "test"
           {:main 'test-runner
            :target :nashorn
            :output-to "target/nashorn/test.js"
            :output-dir "target/nashorn/out"
            :optimizations :simple})
