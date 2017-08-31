(ns example.core
  (:gen-class)
  (:require [hiccup2.core :as hiccup]
            [kuuga.growing :as growing]
            [kuuga.tool :as tool]
            [kuuga.ultimate :as ultimate]
            [ring.adapter.jetty :as jetty]
            [ring.util.response :as response]))

(defonce server (atom nil))

(defn layout [& contents]
  (hiccup/html
   {:mode :html}
   (hiccup/raw "<!doctype html>")
   [:html
    [:head
     [:title "Bootstrap - Example"]
     [:link {:rel "stylesheet"
             :href "https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css"
             :integrity "sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M"
             :crossorigin "anonymous"}]]
    [:body
     [:nav.navbar.navbar-dark.bg-dark
      [:a.navbar-brand {:href "#"} "Bootstrap - Example"]]
     [:div.container contents]]]))

(defn naive-form [{:keys [errors values] :as opts}]
  [:form
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

;;; Be careful these transformation rules are just for this example
(defn invalid-feedback [opts tagopts]
  (when-let [err-msg (get-in opts [:errors (:name tagopts)])]
    [:div.invalid-feedback err-msg]))

(defmethod growing/transform-by-class :form-group
  [_ options tag-vector]
  (let [[tagname tagopts contents] (tool/parse-tag-vector tag-vector)
        contents (seq (reduce (fn [contents' tagvec']
                                (let [[tn to _] (tool/parse-tag-vector tagvec')
                                      [_ t] (tool/parse-tag-name tn)]
                                  (cond-> (conj contents' tagvec')
                                    (= t "input") (conj `(invalid-feedback ~options ~to)))))
                              []
                              contents))]
    `[~tagname
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
  (let [[tagname tagopts contents] (tool/parse-tag-vector tag-vector)]
    `[~tagname
      (update-input-opts ~options ~tagopts)
      ~@contents]))

(defn macro-form [opts]
  (ultimate/transform
   opts
   [:form
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

(defn app [req]
  (let [opts {:values {:email "foobar" :password "p@sswOad"}
              :errors {:email "please input your email address"}}]
    (-> (layout
         [:h1 "Naive form"]
         (naive-form opts)
         [:h1 "Transformed form"]
         (macro-form opts))
        str
        response/response
        (response/content-type "text/html; charset=utf8"))))

(defn start-server []
  (when-not @server
    (reset! server (jetty/run-jetty app {:port 3000 :join? false}))))

(defn stop-server []
  (when @server
    (.stop @server)
    (reset! server nil)))

(defn -main
  [& args]
  (start-server))
