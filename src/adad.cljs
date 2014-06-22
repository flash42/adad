(ns adad
  (:require
    [dommy.utils :as utils]
    [dommy.core :as dommy])
  (:use-macros
    [dommy.macros :only [node sel sel1]]))

(defn- add-row [tbl col-num row-idx]
  (let [row (node [:tr])]
    (dommy/append! tbl row)
    (dotimes [col-idx col-num]
      (dommy/append! row (node [:td {:class (str row-idx col-idx)}] )))))

(defn- create-board [size]
    (dotimes [r size]
      (add-row (sel1 :.board) size r)))

(def game-state
  [["2" "0" "0" "0"]
   ["0" "0" "0" "2"]
   ["2" "0" "0" "0"]
   ["0" "0" "0" "0"]])


(defn- class-sel1 [style-class]
  (.item
   (.getElementsByClassName js/document style-class) 0))

(defn- update-cell! [row col value]
  (dommy/set-text! (class-sel1 (str row col)) value))

(defn- update! [state]
  (doseq [[x row](map-indexed vector state)
          [y value] (map-indexed vector row)]
      (update-cell! x y value)))

(defn ^:export gameloop []
  (do
    (create-board 4)
    (update! game-state))


