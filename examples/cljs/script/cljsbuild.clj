(ns cljsbuild
  (:require [cljs.build.api :as api]
            example.kuuga-ext))

(api/build "src-cljs"
           {:main 'example.core
            :output-to "resources/public/js/main.js"
            :output-dir "target/cljs/out"
            :optimizations :advanced})
