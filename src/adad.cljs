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
  (doseq [[x row] (map identity state)
          [y value] (map identity row)]
      (update-cell! x y value)))

(defn ^:export gameloop []
  (do
    (create-board 4)
    (update! game-state)))

(defn step-state
  [evt]
  ; left = 37
  ; up = 38
  ; right = 39
  ; down = 40
  (when (= 37 (.-keyCode evt))
    (update! (calc-state! :left game-state))))

(dommy/listen! (sel1 :body) :keyup step-state!)

(defn get-col [col-idx state]
  (apply hash-map
         (flatten
          (map #(list (first %) (get (second %) col-idx))
               (seq state)))))

(defn get-row [row-idx state]
  (get state row-idx))

(defn merge-left [row-idx state]
  (let [row (get state row-idx)]
    (for [[col-idx val]
          (map-indexed list
                       (filter #(not= 0 (second %)) (get-row row-idx state)))]
      (assoc state row-idx (assoc {0 0, 1 0, 2 0, 3 0} col-idx val)))))

(merge-left 0 game-state)

(def
  game-state
  (assoc game-state 0 (assoc (get game-state 0) 0 5)))
game-state



