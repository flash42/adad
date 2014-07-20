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
        (rand-nth
         (for [[x row] (map-indexed vector stage)
               [y val] (map-indexed vector row)
               :when (= 0 val)]
           [x y]))]
    (do
      (set-state! (assoc stage c (assoc (vec (get stage c)) r 2)))
      game-state)))



(defn- add-random-element! [stage]
  (wait 400 (fn [] (update-ui! (add-rand-to-stage! stage)))))

(defn- next-round! [stage]
  (cond
   (empty?
    (filter #(= 0 %) (flatten stage)))
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

