(ns adad
  (:require
   [dommy.core :as dommy]
   [adad-ui :as ui])
  (:use [adad-ui :only [update-ui! create-board]]
        [adad-stage :only [game-state]]
        [game-logic :only [game-key-handler!]])
  (:use-macros
    [dommy.macros :only [sel1]]))


(defn ^:export init []
  (do
    (create-board 4)
    (update-ui! game-state)))


(dommy/listen! (sel1 :body) :keyup game-key-handler!)
