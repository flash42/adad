(ns adad
  (:require
   [dommy.core :as dommy]
   [adad-ui :as ui])
  (:use [adad-ui :only [update! create-board]]
        [adad-stage :only [game-state]]
        [game-logic :only [step-state!]])
  (:use-macros
    [dommy.macros :only [sel1]]))


(defn ^:export gameloop []
  (do
    (create-board 4)
    (update! game-state)))


(dommy/listen! (sel1 :body) :keyup step-state!)
