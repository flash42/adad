(ns game-logic
  (:use [adad-ui :only [update-ui!]]
        [adad-stage :only
         [game-state set-state! merge-right merge-left merge-up merge-down]]))


(defn calc-state! [dir state]
  (let [updated-state
        (cond
         (= dir :left)
         (merge-left state)
         (= dir :right)
         (merge-right state)
         (= dir :up)
         (merge-up state)
         (= dir :down)
         (merge-down state)
         )]
    (do
      (set-state! updated-state)
      game-state)))

(defn add-rand-to-stage! [stage]
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

(defn wait [ms func]
  (js* "setTimeout(~{func}, ~{ms})"))

(defn add-random-element! [stage]
  (wait 400 (fn [] (update-ui! (add-rand-to-stage! stage)))))

(defn next-round! [stage]
  (cond
   (empty?
    (flatten
     (map
      (fn [pair]
        (filter #(not (= 0 %))
                (map #(val %) (val pair)))) (map identity stage))))
   (js/alert "End of game")
   :else
   (add-random-element! stage)))


;; Event handling
(defn game-key-handler!
  [evt]
  (cond
   (= 37 (.-keyCode evt))
   (do (update-ui! (calc-state! :left game-state))
     (next-round! game-state))
   (= 39 (.-keyCode evt))
   (do (update-ui! (calc-state! :right game-state))
     (next-round! game-state))
   (= 38 (.-keyCode evt))
   (do (update-ui! (calc-state! :up game-state))
     (next-round! game-state))
   (= 40 (.-keyCode evt))
   (do (update-ui! (calc-state! :down game-state))
     (next-round! game-state))))

