(ns example.macro
  (:require [kuuga.growing :as growing]
            [kuuga.tool :as tool]
            [kuuga.ultimate :as ultimate]
            [criterium.core :as criterium]))

(remove-all-methods growing/transform-by-tag)
(remove-all-methods growing/transform-by-class)
(load "/kuuga/growing")

(defn invalid-feedback [opts tagopts]
  (let [err-msg (get-in opts [:errors (:name tagopts)])]
    [:div.invalid-feedback err-msg]))

(defmethod growing/transform-by-class :form-group
  [_ options tag-vector]
  (let [[tagkw tagopts contents] (tool/parse-tag-vector tag-vector)
        contents (reduce (fn [contents' tagvec']
                           (let [[tk to _] (tool/parse-tag-vector tagvec')
                                 [_ t] (tool/parse-tag-keyword tk)]
                             (cond-> (conj contents' tagvec')
                               (= t "input") (conj `(invalid-feedback ~options ~to)))))
                         []
                         contents)]
    `[~tagkw
      ~tagopts
      ~@contents]))

(defn update-input-opts [opts tagopts]
  (let [{:keys [values errors]} opts
        tname (:name tagopts)]
    (-> tagopts
        (assoc :class (if (contains? errors tname) "is-invalid" "is-valid"))
        (cond-> (some? (get values tname)) (assoc :value (get values tname))))))

(defmethod growing/transform-by-tag :input
  [_ options tag-vector]
  (let [[tagkw tagopts contents] (tool/parse-tag-vector tag-vector)]
    `[~tagkw
      (update-input-opts ~options ~tagopts)
      ~@contents]))

(defn macro-form [opts]
  (ultimate/transform
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
