(ns example.simple-test
  (:require [clojure.test :refer :all]
            [snap.core :as s]))

(deftest simple-test
  (s/match-snapshot ::range (range 10))
  (s/match-snapshot ::zipmap (zipmap "snap" (range))))
