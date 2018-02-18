(ns moviola.core
  #?@(:clj [(:require [clojure.java.io :as io]
                      [clojure.edn :as edn]
                      [clojure.test :as t]
                      [clojure.pprint :as p])]
      :cljs [(:require [cljs-node-io.core :as io :refer [slurp spit]]
                       [cljs.reader :as edn]
                       [cljs.test :as t]
                       [cljs.pprint :as p])]))

(defn match-snapshot [k v]
  (let [file-name (str "test/snapshots/" (namespace k) "." (name k) ".edn")
        file (io/file file-name)]
    (if (.exists file)
      (let [snapshot (edn/read-string (slurp file))]
        (t/is (= snapshot v) k))
      (do
        (io/make-parents file-name)
        (spit file-name (with-out-str (p/pprint v)))))))
