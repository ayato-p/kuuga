(defproject ayato_p/kuuga "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "MIT"
            :url "https://choosealicense.com/licenses/mit"}

  :profiles
  {:provided {:dependencies [[org.clojure/clojure "1.8.0"]]}
   :1.5 {:dependencies [[org.clojure/clojure "1.5.1"]]}
   :1.6 {:dependencies [[org.clojure/clojure "1.6.0"]]}
   :1.7 {:dependencies [[org.clojure/clojure "1.7.0"]]}
   :1.8 {:dependencies [[org.clojure/clojure "1.8.0"]]}
   :plugins/cloverage {:plugins [[lein-cloverage "1.0.9"]]}

   :merged [:1.5 :1.6 :1.7 :1.8
            :plugins/cloverage]}

  :aliases
  {"cloverage" ["with-profile" "+plugins/cloverage" "cloverage" "--codecov"]
   "all" ["with-profile" "+1.5:+1.6:+1.7:+1.8"]})
