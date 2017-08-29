(ns example.core
  (:require [kuuga.ultimate :as ultimate :include-macros true]
            [rum.core :as rum]))

(defn invalid-feedback [opts tagopts]
  (let [err-msg (get-in opts [:errors (:name tagopts)])]
    [:div.invalid-feedback err-msg]))

(defn update-input-opts [opts tagopts]
  (let [{:keys [values errors]} opts
        tname (:name tagopts)]
    (-> tagopts
        (assoc :class (if (contains? errors tname) "is-invalid" "is-valid"))
        (cond-> (some? (get values tname)) (assoc :value (get values tname))))))

(def opts
  {:values {:email "foobar" :password "p@sswOad"}
   :errors {:email "please input your email address"}})

(rum/defc macro-form [opts]
  [:div
   [:h1 "Transformed form"]
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
     [:button.btn.btn-primary {:type :submit} "Submit"]])])

(defn main []
  (let [elm (js/document.querySelector "div.container")]
    (rum/mount (macro-form opts) elm)))

(main)
