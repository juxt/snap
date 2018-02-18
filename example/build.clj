(require '[cljs.build.api :as api]
         '[clojure.java.shell :as shell]
         '[clojure.string :as string])

;;; Configuration.

(def source-dir "src")

(def test-dir "test")

(def test-config {:main          'example.test-runner
                  :output-to     "target/test.js"
                  :output-dir    "target/test"
                  :target        :nodejs
                  :optimizations :none
                  :source-map    true})

(def test-environment {:TEST true})

;;; Tasks mechanism.

(defmulti task first)

(defmethod task :default
  [args]
  (let [all-tasks (-> task methods (dissoc :default) keys sort (->> (interpose ", ") (apply str)))]
    (println "unknown or missing task argument. Choose one of:" all-tasks)
    (System/exit 1)))


;;; Helper functions.

(defn run-node-tests []
  (let [{:keys [out err exit]} (shell/sh "node" "target/test.js" :env test-environment)]
    (println out err)
    (= exit 0)))

(defn try-require [ns-sym]
  (try (require ns-sym) true (catch Exception e false)))

(defmacro with-namespaces
  [namespaces & body]
  (if (every? try-require namespaces)
    `(do ~@body)
    `(do (println "task not available - required dependencies not found")
         (System/exit 1))))


;;; Testing task

(defn test-once []
  (api/build (api/inputs source-dir test-dir) test-config)
  (let [success? (run-node-tests)]
    (System/exit (if success? 0 1))))

(defn test-refresh []
  (api/watch (api/inputs source-dir test-dir)
             (assoc test-config :watch-fn run-node-tests)))

(defmethod task "test" [[_ type]]
  (case type
    (nil "once") (test-once)
    "watch"      (test-refresh)
    (do (println "Unknown argument to test task:" type)
        (System/exit 1))))


;;; Build script entrypoint.

(task *command-line-args*)
