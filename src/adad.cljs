(ns adad
  (:require
   [dommy.core :as dommy]
   [adad.ui :as ui])
  (:use [adad.ui :only [update-ui! create-board]]
        [adad.stage :only [game-state]]
        [adad.game :only [game-key-handler!]]
        [adad.utils :only [wait]])
  (:use-macros
    [dommy.macros :only [sel1]]))


(defn ^:export init []
  (do
    (create-board 4)
    (update-ui! @game-state)))

(add-watch game-state
           :redraw (fn [_ _ _ new] (wait 200 (fn [] (update-ui! new)))))

(dommy/listen! (sel1 :body) :keyup game-key-handler!)
