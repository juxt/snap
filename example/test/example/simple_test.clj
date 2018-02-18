(ns example.simple-test
  (:require [clojure.test :refer :all]
            [moviola.core :as m]))

(deftest simple-test
  (m/match-snapshot ::range (range 10))
  (m/match-snapshot ::zipmap (zipmap "moviola" (range))))
