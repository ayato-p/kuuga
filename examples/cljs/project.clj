(defproject example "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [[ayato_p/kuuga "0.1.0-SNAPSHOT"]
                 [org.clojure/clojure "1.8.0"]
                 [ring "1.6.2"]
                 [rum "0.10.8"]]
  :main ^:skip-aot example.core
  :target-path "target/%s"
  :profiles
  {:dev {:dependencies [[com.cemerick/piggieback "0.2.2"]]}
   :provided {:dependencies [[org.clojure/clojurescript "1.9.908"]]}
   :uberjar {:aot :all}
   :tasks/cljsbuild {:source-paths ["src" "src-cljs"]}
   :plugins/exec {:plugins [[lein-exec "0.3.6"]]}}

  :aliases
  {"exec" ["with-profile" "+plugins/exec" "exec"]
   "cljsbuild" ["with-profile" "+tasks/cljsbuild" "exec" "-p" "script/cljsbuild.clj"]})
