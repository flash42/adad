(ns adad.stage)

;; Initial Game state
(def game-state
  [[2, 2, 0, 2],
   [0, 0, 0, 0],
   [0, 0, 0, 0],
   [0, 0, 0, 0]])


(defn- filter-zeros [row]
  (filter #(not= 0 %) row))

(defn- merge-sum [[start & coll]]
  (if (nil? start) ()
  (flatten (reverse
            (reduce
             #(if (= %2 (first %1))
                (cons (list (+ %2 (first %1))) (rest %1))
                (cons %2 %1)) (list start) coll)))))

(defn- zero-pad [len coll]
  (concat coll (take (- len (count coll)) (repeat 0))))

(defn- calc-row [row]
  (zero-pad (count row) (merge-sum
                         (filter-zeros row))))

(defn- calc-left-merge [state]
  (map calc-row state))

(defn- calc-right-merge [state]
  (map #(reverse (calc-row (reverse %))) state))


(defn- pivot [state]
  (let [last-idx (count state)]
    (into []
          (map (fn [pos]
                 (into [ ]
                       (map (fn [row] (nth row pos)) state))) (range 0 last-idx)))))

;; Public API
(defn set-state! [state]
  (def game-state (vec state)))

(defn merge-left [state]
  (calc-left-merge state))

(defn merge-right [state]
  (calc-right-merge state))

(defn merge-up [state]
  (pivot
   (calc-left-merge (pivot state))))

(defn merge-down [state]
  (pivot
   (calc-right-merge (pivot state))))
