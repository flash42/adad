(ns adad.core-test
  (:require-macros [cemerick.cljs.test
                    :refer (is deftest with-test run-tests testing test-var)])
  (:require
   [cemerick.cljs.test :as test])
  (:use [adad.stage :only
         [game-state set-state! merge-right merge-left merge-up merge-down]]))

(deftest computes-left-merge
  (do
    (is (= (list (list 4 2 0)) (merge-left [[2 2 2]])))
    (is (= (list (list 4 0 0)) (merge-left [[2 0 2]])))
    (is (= (list (list 4 4 0 0 0)) (merge-left [[0 2 2 2 2]])))
    (is (= (list (list 4 0 0) (list 4 0 0)) (merge-left [[0 2 2] [2 2 0]])))))

(deftest computes-right-merge
  (do
    (is (= (list (list 0 2 4)) (merge-right [[2 2 2]])))
    (is (= (list (list 0 0 4)) (merge-right[[2 0 2]])))
    (is (= (list (list 0 0 0 4 4)) (merge-right [[0 2 2 2 2]])))
    (is (= (list (list 0 0 4) (list 0 0 4)) (merge-right [[0 2 2] [2 2 0]])))))

(deftest computes-up-merge
  (do
    (is (list (list 4 0) (list 0 0)) (merge-up [[2 0] [2 0]]))
    (is (list (list 4 0 0) (list 2 0 0) (list 0 0 0)) (merge-up [[2 0 0] [2 0 0] [2 0 0]]))
    (is (list (list 4 0 0) (list 2 0 0) (list 0 0 0)) (merge-up [[2 0 0] [2 0 0] [2 0 0]]))))

(deftest computes-down-merge
  (do
    (is (list (list 0 0) (list 4 0)) (merge-down [[2 0] [2 0]]))
    (is (list (list 0 0 0) (list 2 0 0) (list 4 0 0)) (merge-down [[2 0 0] [2 0 0] [2 0 0]]))
    (is (list (list 0 0 0) (list 2 0 0) (list 4 0 0)) (merge-down [[2 0 0] [2 0 0] [2 0 0]]))))
