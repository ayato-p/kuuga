(ns kuuga.ultimate-test
  (:require [clojure.test :as t]
            [kuuga.growing :as k.growing]
            [kuuga.tool :as k.tool]
            [kuuga.ultimate :as k.ultimate]
            [test-helper :as helper]))

(helper/reset-multifn)

(defn- update-opts-for-input [tagopts]
  (cond-> tagopts
    (= (:name tagopts) :weapon) (assoc :value :sword)))

(defn- update-opts-for-kuuga [tagopts tagname]
  (let [[_ _ _ classes] (k.tool/parse-tag-name tagname)
        classes (->> (cond-> clz (string? clz) vector)
                     (as-> (:class tagopts) clz)
                     (concat classes)
                     set)]
    (cond-> tagopts
      (not (classes "form")) (assoc :class ["growing" "form"]))))

(defmethod k.growing/transform-by-tag :input
  [_ opts tagvec]
  (let [[tagname tagopts contents] (k.tool/parse-tag-vector tagvec)]
    `[~tagname
      (update-opts-for-input ~tagopts)
      ~@contents]))

(defmethod k.growing/transform-by-id :kuuga
  [_ opts tagvec]
  (let [[tagname tagopts contents] (k.tool/parse-tag-vector tagvec)]
    `[~tagname
      (update-opts-for-kuuga ~tagopts ~tagname)
      ~@contents]))

(t/deftest transform*-test
  (t/testing "simple test cases"
    (t/are [src result] (= (k.ultimate/transform* src) result)
      [:div#kuuga.mighty.form] [:div#kuuga.mighty.form nil]
      [:div#kuuga] [:div#kuuga {:class ["growing" "form"]}]
      [:div#gurongi] [:div#gurongi])

    (t/are [src result] (= (k.ultimate/transform* src) result)
      [:div#kuuga.mighty.form [:input {:name :bike}]]
      [:div#kuuga.mighty.form nil [:input {:name :bike}]]

      [:div#kuuga.mighty.form [:input {:name :weapon}]]
      [:div#kuuga.mighty.form nil [:input {:name :weapon :value :sword}]])))
