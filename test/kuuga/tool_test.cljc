(ns kuuga.tool-test
  (:require [kuuga.tool :as k.tool]
            #?(:clj [clojure.test :as t]
               :cljs [cljs.test :as t])))

(t/deftest parse-tag-keyword-test
  (t/testing "simple test cases"
    (t/are [tag-keyword result] (= (k.tool/parse-tag-keyword tag-keyword)
                                   result)
      :div#kuuga.mighty.form ["div#kuuga.mighty.form" "div" "kuuga" ["mighty" "form"]]
      :div#kuuga.ultimate.form ["div#kuuga.ultimate.form" "div" "kuuga" ["ultimate" "form"]]
      :div#kuuga ["div#kuuga" "div" "kuuga" nil]
      :div ["div" "div" nil nil]

      "div#kuuga.mighty.form" ["div#kuuga.mighty.form" "div" "kuuga" ["mighty" "form"]]
      "div#kuuga.ultimate.form" ["div#kuuga.ultimate.form" "div" "kuuga" ["ultimate" "form"]]
      "div#kuuga" ["div#kuuga" "div" "kuuga" nil]
      "div" ["div" "div" nil nil]

      nil nil)))

(t/deftest parse-tag-vector-test
  (t/testing "simple test cases"
    (t/are [tag-vector result] (= (k.tool/parse-tag-vector tag-vector)
                                  result)

      [:div#kuuga {:class ["mighty" "form"]}
       [:input {:type :hidden :name :tool :value :none}]]
      [:div#kuuga {:class ["mighty" "form"]}
       '([:input {:type :hidden :name :tool :value :none}])]

      [:div#kuuga [:input {:type :hidden :name :tool :value :none}]]
      [:div#kuuga nil '([:input {:type :hidden :name :tool :value :none}])]

      [:div#kuuga {:class ["growing" "form"]}]
      [:div#kuuga {:class ["growing" "form"]} nil]

      [:div#kuuga]
      [:div#kuuga nil nil]

      [1 2 3]
      [1 nil '(2 3)])))
