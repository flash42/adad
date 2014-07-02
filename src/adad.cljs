(ns adad
  (:require
    [dommy.utils :as utils]
    [dommy.core :as dommy])
  (:use-macros
    [dommy.macros :only [node sel sel1]]))


(def game-state
  {0 {0 2, 1 0, 2 0, 3 0},
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
      (dommy/append! row (node [:td {:class (str row-idx col-idx)}] )))))

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

;; Event handling
(defn step-state
  [evt]
  ; left = 37
  ; up = 38
  ; right = 39
  ; down = 40
  (when (= 37 (.-keyCode evt))
    (update! (calc-state! :left game-state))))

(dommy/listen! (sel1 :body) :keyup step-state!)


;; Update
(defn get-col [col-idx state]
  (apply hash-map
         (flatten
          (map #(list (first %) (get (second %) col-idx))
               (seq state)))))

(defn get-row [row-idx state]
  (get state row-idx))

(defn filter-zeros [row]
  (map #(second %1) (filter #(not= 0 (second %)) row)))


(defn m2 [prev tail acc]
  (cond
   (= (count tail) 0)
   (reverse (if (= prev (first tail))
              (cons (+ prev (first tail)) acc)
              (cons prev acc)))
   (= (count tail) 1)
   (reverse (if (= prev (first tail))
              (cons (+ prev (first tail)) acc)
              (cons (first tail) (cons prev acc))))
   :else (recur (first (rest tail))
             (rest (rest tail))
             (if (= prev (first tail))
               (cons (+ prev (first tail)) acc)
               (cons (first tail) (cons prev acc))))))


(loop [prev 1 tail [1 2] acc []]
  (cond
   (<= (count tail) 1)
   (reverse
    (if (= prev (first tail))
      (cons (+ prev (first tail)) acc)
      (if (nil? (first tail))
        (cons prev acc)
        (cons (first tail) (cons prev acc)))))
   :else (recur (first (rest tail))
             (rest (rest tail))
             (if (= prev (first tail))
               (cons (+ prev (first tail)) acc)
               (cons (first tail) (cons prev acc))))))

(m2 3 [1 2 2 2 2 5 5 5] [])

(defn merge-sum [[start & coll]]
  (flatten (reverse
            (reduce
             #(if (= %2 (first %1))
                (cons (list (+ %2 (first %1))) (rest %1))
                (cons %2 %1)) (list start) coll))))


(def game-state2
  {0 {0 2, 1 3, 2 0, 3 3},
   1 {0 2, 1 0, 2 0, 3 0},
   2 {0 2, 1 0, 2 0, 3 0},
   3 {0 2, 1 0, 2 0, 3 0}})


(zero-pad 4 (merge-sum (reverse (filter-zeros (get game-state2 0)))))

(defn zero-pad [len coll]
  (concat coll (take (- len (count coll)) (repeat 0))))








