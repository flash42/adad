(ns adad
  (:require
    [dommy.utils :as utils]
    [dommy.core :as dommy])
  (:use-macros
    [dommy.macros :only [node sel sel1]]))


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
  (dommy/set-text! (class-sel1
                    (str row col))
                   (if (not (= 0 value)) value)))

(defn- update! [state]
  (doseq [[x row] (map identity state)
          [y value] (map identity row)]
      (update-cell! x y value)))

(def game-state
  {0 {0 2, 1 2, 2 0, 3 2},
   1 {0 2, 1 0, 2 0, 3 0},
   2 {0 2, 1 0, 2 0, 3 0},
   3 {0 2, 1 0, 2 0, 3 0}})

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

(defn merge-sum [[start & coll]]
  (if (nil? start) ()
  (flatten (reverse
            (reduce
             #(if (= %2 (first %1))
                (cons (list (+ %2 (first %1))) (rest %1))
                (cons %2 %1)) (list start) coll)))))

(defn zero-pad [len coll]
  (concat coll (take (- len (count coll)) (repeat 0))))

(defn mapify [coll]
  (apply hash-map (flatten (map-indexed vector coll))))

(defn calc-row [row]
  (zero-pad (count row) (merge-sum
                         (filter-zeros row))))

(defn calc-left-merge [state]
  (map #(mapify (calc-row (second %))) (map identity state)))

(defn calc-right-merge [state]
  (map #(mapify (reverse (calc-row (reverse (second %))))) (map identity state)))



(defn pivot [state]
  (let [size (count (second (first state)))]
    (apply hash-map
           (interleave (range 0 size)
                       (map mapify (apply map vector
                                          (map #(map second (second %)) state)))))))


(defn calc-state! [dir state]
  (let [updated-state
        (cond
         (= dir :left)
         (apply hash-map
                (flatten(map-indexed vector (calc-left-merge state))))
         (= dir :right)
         (apply hash-map
                (flatten(map-indexed vector (calc-right-merge state))))
         (= dir :up)
         (pivot
          (apply hash-map
                 (flatten (map-indexed vector (calc-left-merge (pivot state))))))
         (= dir :down)
         (pivot
          (apply hash-map
                 (flatten (map-indexed vector (calc-right-merge (pivot state))))))
         )]
    (do
      (def game-state updated-state)
      updated-state)))


;; Game logic
(defn add-rand! [stage]
  (let [[c, r]
        (let [[col-idx, row-idxs]
              (rand-nth
               (filter
                #(not (empty? (second %)))
                (map
                (fn [pair]
                  (list
                   (first pair)
                   (map #(first %) (filter #(= 0 (second %))
                                           (seq (second pair))))))
                (seq stage))))]
          (list col-idx (rand-nth row-idxs)))]
    (do
      (def game-state
        (assoc stage c (assoc (get stage c) r 2))))
    game-state))

(defn step-next! [stage]
  (cond
   (empty?
    (flatten
     (map
      (fn [pair]
        (filter #(not (= 0 %))
                (map #(val %) (val pair)))) (map identity stage))))
   (js/alert "End of game")
   :else
   (add-rand! stage)))


(defn wait [ms func]
  (js* "setTimeout(~{func}, ~{ms})"))

(defn add-random-element! []
  (wait 400 (fn [] (update! (step-next! game-state)))))

;; Event handling
(defn step-state!
  [evt]
  ; left = 37
  ; up = 38
  ; right = 39
  ; down = 40
  (cond
   (= 37 (.-keyCode evt))
   (do
     (update! (calc-state! :left game-state))
     (add-random-element!))
   (= 39 (.-keyCode evt))
   (do (update! (calc-state! :right game-state))
     (add-random-element!))
   (= 38 (.-keyCode evt))
   (do (update! (calc-state! :up game-state))
     (add-random-element!))
   (= 40 (.-keyCode evt))
   (do (update! (calc-state! :down game-state))
     (add-random-element!))))

;;(update! (add-rand! {0 {0 0, 1 1}}))

(dommy/listen! (sel1 :body) :keyup step-state!)
