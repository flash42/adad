(ns adad.game
  (:use
   [adad.ui :only [update-ui!]]
   [adad.utils :only [wait]]
   [adad.stage :only
    [game-state set-state! merge-right merge-left merge-up merge-down]]))


(defn- merge-stage! [dir state]
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

(defn- add-rand-to-stage! [stage]
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

(defn- add-random-element! [stage]
  (wait 400 (fn [] (update-ui! (add-rand-to-stage! stage)))))

(defn- next-round! [stage]
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
(def key-left 37)
(def key-right 39)
(def key-up 38)
(def key-down 40)


(defn- handle-key [dir]
  (update-ui! (merge-stage! dir game-state))
  (next-round! game-state))

(defn game-key-handler!
  [evt]
  (cond
   (= key-left (.-keyCode evt))
   (handle-key :left)
   (= key-right (.-keyCode evt))
   (handle-key :right)
   (= key-up (.-keyCode evt))
   (handle-key :up)
   (= key-down (.-keyCode evt))
   (handle-key :down)))

