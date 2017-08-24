(ns test-runner
  (:require [cljs.test :as t :include-macros true]
            kuuga.ultimate-cljs-test
            kuuga.mighty-test
            kuuga.tool-test))

(set! *print-fn* js/print)

(t/run-tests 'kuuga.tool-test
             'kuuga.mighty-test
             'kuuga.ultimate-cljs-test)
