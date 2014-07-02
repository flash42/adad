(ns adad
  (:require
    [dommy.utils :as utils]
    [dommy.core :as dommy])
  (:use-macros
    [dommy.macros :only [node sel sel1]]))


(def game-state
  {0 {0 2, 1 0, 2 0, 3 2},
   1 {0 2, 1 0, 2 0, 3 0},
   2 {0 2, 1 0, 2 0, 3 0},
   3 {0 2, 1 0, 2 0, 3 0}})

;; Utils
(defn- class-sel1 [style-class]
  (.item
   (.getElementsByClassName js/document style-class) 0))

;; Render UI
(defn- add-row [tbl col-num row-idx]
  (let [row (node [:tr])]
    (dommy/append! tbl row)
    (dotimes [col-idx col-num]
      (dommy/append! row (node [:td {:class (str row-idx col-idx)}])))))

(defn- create-board [size]
    (dotimes [r size]
      (add-row (sel1 :.board) size r)))

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

(update! game-state)


;; Caculate next state
(defn get-col [col-idx state]
  (apply hash-map
         (flatten
          (map #(list (first %) (get (second %) col-idx))
               (seq state)))))

(defn get-row [row-idx state]
  (get state row-idx))

(defn filter-zeros [row]
  (map #(second %1) (filter #(not= 0 (second %)) row)))

(def game-state2
  {0 {0 2, 1 3, 2 0, 3 3},
   1 {0 2, 1 0, 2 0, 3 0},
   2 {0 2, 1 0, 2 0, 3 0},
   3 {0 2, 1 0, 2 0, 3 0}})

(defn merge-sum [[start & coll]]
  (flatten (reverse
            (reduce
             #(if (= %2 (first %1))
                (cons (list (+ %2 (first %1))) (rest %1))
                (cons %2 %1)) (list start) coll))))

(defn zero-pad [len coll]
  (concat coll (take (- len (count coll)) (repeat 0))))

(defn calc-row [row]
  (apply hash-map
         (flatten (map-indexed vector
                               (zero-pad (count row) (merge-sum
                                            (filter-zeros row)))))))

(defn calc-left-merge [state]
  (map #(calc-row (second %)) (map identity state)))

(defn calc-state! [dir state]
  (let [updated-state
        (cond
         (= dir :left)
         (apply hash-map
                (flatten(map-indexed vector (calc-left-merge state)))))]
    (do
      (def game-state updated-state)
      updated-state)))

;; (calc-state! :left game-state2)


;; Event handling
(defn step-state!
  [evt]
  ; left = 37
  ; up = 38
  ; right = 39
  ; down = 40
  (when (= 37 (.-keyCode evt))
    (update! (calc-state! :left game-state))))

;;(calc-state! :left game-state)

(dommy/listen! (sel1 :body) :keyup step-state!)
