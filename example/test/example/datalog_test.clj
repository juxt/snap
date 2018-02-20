(ns example.datalog-test
  (:require [clojure.test :refer :all]
            [snap.core :as s]
            [datascript.core :as d]))

(defn seed-data [conn]
  (d/transact! conn (for [i (range 10)]
                      {:name (str "Customer-" i)
                       :age (* 2 i)})))

(deftest datalog-test
  (let [conn (d/create-conn)]
    (seed-data conn)
    (s/match-snapshot ::datalog (d/q '[:find  ?n ?a :where [?e :name ?n] [?e :age ?a]] @conn))))
