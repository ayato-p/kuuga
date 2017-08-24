(ns example.function
  (:require [kuuga.growing :as growing]
            [kuuga.mighty :as mighty]
            [kuuga.tool :as tool]))

(remove-all-methods growing/transform-by-class)
(remove-all-methods growing/transform-by-tag)
(load "/kuuga/growing")

(defn- inject-invalid-feedback-tag [options contents]
  (seq (reduce
        (fn [contents' tagvec]
          (let [[tk to _] (tool/parse-tag-vector tagvec)
                [_ t] (tool/parse-tag-keyword tk)
                tn (:name to)]
            (cond-> (conj contents' tagvec)
              (= t "input")
              (conj [:div.invalid-feedback (get-in options [:errors tn])]))))
        []
        contents)))

(defmethod growing/transform-by-class :form-group
  [_ options tag-vector]
  (let [[tagkw tagopts contents] (tool/parse-tag-vector tag-vector)
        contents (inject-invalid-feedback-tag options contents)]
    (cond-> [tagkw]
      (seq tagopts) (conj tagopts)
      (seq contents) (conj contents))))

(defn- update-tagopts
  [options tagopts]
  (let [{:keys [values errors]} options
        tagname (:name tagopts)]
    (-> tagopts
        (assoc :class (if (contains? errors tagname) "is-invalid" "is-valid"))
        (cond-> (some? (get values tagname)) (assoc :value (get values tagname))))))

(defmethod growing/transform-by-tag :input
  [_ options tag-vector]
  (let [[tagkw tagopts contents] (tool/parse-tag-vector tag-vector)
        tagopts (update-tagopts options tagopts)]
    (cond-> [tagkw]
      (seq tagopts) (conj tagopts)
      (seq contents) (conj contents))))

(defn function-form [opts]
  (mighty/transform
   opts
   [:form {:method :post}
    [:div.form-group
     [:label {:for "input-email"} "Email address"]
     [:input#input-email.form-control
      {:type :email :name :email :placeholer "Enter email"}]
     [:small.form-text.text-muted
      "We'll never share your email with anyone else."]]
    [:div.form-group
     [:label {:for "input-password"} "Password"]
     [:input#input-password.form-control
      {:type :password :name :password :placeholer "Password"}]]
    [:button.btn.btn-primary {:type :submit} "Submit"]]))
