(ns moviola.core
  #?@(:clj [(:require [clojure.java.io :as io]
                      [clojure.edn :as edn]
                      [clojure.test :as t]
                      [clojure.pprint :as p])]
      :cljs [(:require [cljs-node-io.core :as io :refer [slurp spit]]
                       [cljs.reader :as edn]
                       [cljs.test :as t]
                       [cljs.pprint :as p])]))

(defn default-make-path
  [ns-kw]
  (str "test/snapshots/" (namespace ns-kw) "." (name ns-kw) ".edn"))

(defn match-snapshot
  "Accepts a unique namespaced keyword and a value.
   Creates a file (using the keyword for its name) if not already present, and writes the value to it.
   If a file with that name is already present it compares its content to the value using a test/is macro."
  ([k v] (match-snapshot {} k v))
  ([{:keys [make-path pprint?] :or {make-path default-make-path pprint? true} :as opts} k v]
   (let [file-name (make-path k)
         file (io/file file-name)]
     (if (.exists file)
       (let [snapshot (edn/read-string (slurp file))]
         (t/is (= snapshot v) k))
       (do
         (io/make-parents file-name)
         (spit file-name (if pprint? (with-out-str (p/pprint v)) v)))))))
