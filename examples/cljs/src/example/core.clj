(ns example.core
  (:gen-class)
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.resource :as resource]
            [ring.util.response :as response]
            [rum.core :as rum]))

(defonce server (atom nil))

(defn layout []
  (rum/render-html
   [:html
    [:head
     [:title "Kuuga Example"]
     [:link {:rel "stylesheet"
             :href "https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css"
             :integrity "sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M"
             :crossorigin "anonymous"}]]
    [:body
     [:nav.navbar.navbar-dark.bg-dark
      [:a.navbar-brand {:href "#"} "Bootstrap - Example"]]
     [:div.container]
     [:script {:src "/js/main.js"}]]]))

(defn handler [req]
  (-> (layout)
      response/response
      (response/content-type "text/html; charset=utf-8")))

(def app
  (resource/wrap-resource handler "public"))

(defn start-server []
  (when (nil? @server)
    (reset! server (jetty/run-jetty #'app {:port 3000 :join? false}))))

(defn stop-server []
  (when @server
    (.stop @server)
    (reset! server nil)))

(defn -main [& args]
  (start-server))
