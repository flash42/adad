(ns adad-stage)

;; Initial Game-state
(def game-state
  {0 {0 2, 1 2, 2 0, 3 2},
   1 {0 0, 1 0, 2 0, 3 0},
   2 {0 0, 1 0, 2 0, 3 0},
   3 {0 0, 1 0, 2 0, 3 0}})

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

(defn set-state! [state]
  (def game-state state))
