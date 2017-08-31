(ns kuuga.ultimate-cljs-test
  (:require [cljs.test :as t :include-macros true]
            [kuuga.tool :as k.tool]
            [kuuga.ultimate :as k.ultimate :include-macros true]))

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
