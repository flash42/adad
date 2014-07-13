(ns game-logic
  (:use [adad-ui :only [update!]]
        [adad-stage :only
         [game-state set-state! pivot calc-left-merge calc-right-merge]]))


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
      (set-state! updated-state)
      game-state)))

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
      (set-state! (assoc stage c (assoc (get stage c) r 2)))
      game-state)))

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

