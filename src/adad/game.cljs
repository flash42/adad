(ns adad.game
  (:use
   [adad.ui :only [update-ui!]]
   [adad.utils :only [wait]]
   [adad.stage :only
    [game-state set-state! merge-right merge-left merge-up merge-down]]))


(defn- merge-stage [dir state]
  (cond
   (= dir :left)
   (merge-left state)
   (= dir :right)
   (merge-right state)
   (= dir :up)
   (merge-up state)
   (= dir :down)
   (merge-down state)))

(defn- add-rand-to-stage [stage]
  (let [[c, r]
        (rand-nth
         (for [[x row] (map-indexed vector stage)
               [y val] (map-indexed vector row)
               :when (= 0 val)]
           [x y]))]
    (assoc stage c (assoc (vec (get stage c)) r 2))))

(defn- next-round [stage]
  (cond
   (empty?
    (filter #(= 0 %) (flatten stage)))
   (do
     (js/alert "End of game")
     stage)
   :else
   (add-rand-to-stage stage)))

;; Event handling
(def key-left 37)
(def key-right 39)
(def key-up 38)
(def key-down 40)

(defn- handle-key! [dir]
  (set-state! (merge-stage dir @game-state))
  (wait 300 (fn [] (set-state! (next-round @game-state)))))

;; TODO push to event bus from here
(defn game-key-handler!
  [evt]
  (cond
   (= key-left (.-keyCode evt))
   (handle-key! :left)
   (= key-right (.-keyCode evt))
   (handle-key! :right)
   (= key-up (.-keyCode evt))
   (handle-key! :up)
   (= key-down (.-keyCode evt))
   (handle-key! :down)))

