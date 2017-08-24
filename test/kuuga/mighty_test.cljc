(ns kuuga.mighty-test
  (:require #?(:clj [clojure.test :as t]
               :cljs [cljs.test :as t])
            [kuuga.growing :as k.growing]
            [kuuga.mighty :as k.mighty]
            [kuuga.tool :as k.tool]
            [test-helper :as helper]))

#?(:clj
   (reset-meta! *ns* {}))

(t/use-fixtures :each
  (fn [f]
    (helper/reset-multifn)
    (f)))

(t/deftest transform*-test
  (t/testing "simple test cases"
    (do
      (defmethod k.growing/transform-by-tag :input
        [_ opts tagvec]
        (let [[tagkw tagopts contents] (k.tool/parse-tag-vector tagvec)
              tagopts (cond-> tagopts
                        (= (:name tagopts) :weapon) (assoc :value :sword))]
          (cond-> [tagkw]
            tagopts (conj tagopts)
            contents (conj contents))))

      (defmethod k.growing/transform-by-id :kuuga
        [_ opts tagvec]
        (let [[tagkw tagopts contents] (k.tool/parse-tag-vector tagvec)
              [_ _ _ classes] (k.tool/parse-tag-keyword tagkw)
              classes (->> (cond-> clz (string? clz) vector)
                           (as-> (:class tagopts) clz)
                           (concat classes)
                           set)
              tagopts (cond-> tagopts
                        (not (classes "form")) (assoc :class ["growing" "form"]))]
          (cond-> [tagkw]
            tagopts (conj tagopts)
            contents (conj contents)))))

    (t/are [src result] (= (k.mighty/transform* src) result)
      [:div#kuuga.mighty.form] [:div#kuuga.mighty.form]
      [:div#kuuga] [:div#kuuga {:class ["growing" "form"]}]
      [:div#gurongi] [:div#gurongi])

    (t/are [src result] (= (k.mighty/transform* src) result)
      [:div#kuuga.mighty.form [:input {:name :bike}]]
      [:div#kuuga.mighty.form '([:input {:name :bike}])]

      [:div#kuuga.mighty.form [:input {:name :weapon}]]
      [:div#kuuga.mighty.form '([:input {:name :weapon :value :sword}])])))

(t/deftest with-transform-ctx-test
  (t/testing "with transform-context test"
    (do
      (defmethod k.growing/transform-by-tag :select
        [_ opts tagvec]
        (let [[tagkw tagopts contents] (k.tool/parse-tag-vector tagvec)
              tname (:name tagopts)]
          (swap! k.growing/+transform-context+ assoc :current-select tname)
          (cond-> [tagkw]
            tagopts (conj tagopts)
            contents (conj contents))))

      (defmethod k.growing/transform-by-tag :option
        [_ opts tagvec]
        (let [values (:values opts)
              [tagkw tagopts contents] (k.tool/parse-tag-vector tagvec)
              cur-select (:current-select @k.growing/+transform-context+)
              tagopts (cond-> tagopts
                        (= (get values cur-select) (:value tagopts))
                        (assoc :selected true))]
          (cond-> [tagkw]
            tagopts (conj tagopts)
            contents (conj contents)))))

    (t/is (= (k.mighty/transform*
              {:values {:form :ultimate}}
              [:select {:name :form}
               [:option {:value :mighty}]
               [:option {:value :ultimate}]])
             [:select {:name :form}
              '([:option {:value :mighty}]
                [:option {:value :ultimate :selected true}])]))))
