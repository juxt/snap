(ns example.datalog-test
  (:require [clojure.test :refer :all]
            [moviola.core :as m]
            [datascript.core :as d]))

(defn seed-data [conn]
  (d/transact! conn (for [i (range 10)]
                      {:name (str "Customer-" i)
                       :age (* 2 i)})))

(deftest datalog-test
  (let [conn (d/create-conn)]
    (seed-data conn)
    (m/match-snapshot ::datalog (d/q '[:find  ?n ?a :where [?e :name ?n] [?e :age ?a]] @conn))))
