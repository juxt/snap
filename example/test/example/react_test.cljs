(ns example.react-test
  (:require [cljs.test :refer [deftest is]]
            [moviola.core :as m]))

(def Renderer (js/require "react-test-renderer"))
(def React (js/require "react"))

(def component
  (React.createElement "ul" nil
    (React.createElement "li" nil "One")
    (React.createElement "li" nil "Two")
    (React.createElement "li" nil "Three")))

(def rendered (Renderer.create component))

(deftest react-test
  (m/match-snapshot ::tree (js->clj (rendered.toJSON))))
