(ns example.test-runner
  (:require [example.react-test]
            [cljs.test :as t]))

(enable-console-print!)

(t/run-tests 'example.react-test)
