(defproject ayato_p/kuuga "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "MIT"
            :url "https://choosealicense.com/licenses/mit"}

  :profiles
  {:dev
   {:dependencies [[com.cemerick/piggieback "0.2.2"]]}

   :provided
   {:dependencies [[org.clojure/clojure "1.9.0-alpha17"]
                   [org.clojure/clojurescript "1.9.908"]]}

   :1.7 {:dependencies [[org.clojure/clojure "1.7.0"]]}
   :1.8 {:dependencies [[org.clojure/clojure "1.8.0"]]}
   :1.9 {:dependencies [[org.clojure/clojure "1.9.0-alpha17"]]}
   :plugins/cloverage {:plugins [[lein-cloverage "1.0.9"]]}
   :plugins/exec {:plugins [[lein-exec "0.3.6"]]}

   :merged [:1.7 :1.8 :1.9
            :plugins/cloverage :plugins/exec]}

  :aliases
  {"cloverage" ["with-profile" "+plugins/cloverage" "cloverage" "--codecov"]
   "exec" ["with-profile" "+plugins/exec" "exec"]
   "cljsbuild" ["exec" "-p" "script/build.clj"]
   "all" ["with-profile" "1.7:1.8:1.9"]})
