(ns example.naive)

(defn naive-form [{:keys [errors values] :as opts}]
  [:form {:method :post}
   [:div.form-group
    [:label {:for "input-email"} "Email address"]
    [:input#input-email.form-control
     {:type :email :name :email
      :class (if (contains? errors :email) "is-invalid" "is-valid")
      :value (:email values)
      :placeholer "Enter email"}]
    [:div.invalid-feedback (:email errors)]
    [:small.form-text.text-muted
     "We'll never share your email with anyone else."]]
   [:div.form-group
    [:label {:for "input-password"} "Password"]
    [:input#input-password.form-control
     {:type :password :name :password
      :class (if (contains? errors :password) "is-invalid" "is-valid")
      :value (:password values)
      :placeholer "Password"}]
    [:div.invalid-feedback (:password errors)]]
   [:button.btn.btn-primary {:type :submit} "Submit"]])
