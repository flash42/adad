(ns adad-ui
    (:require
     [dommy.utils :as utils]
     [dommy.core :as dommy])
    (:use [adad-utils :only [class-sel1]])
  (:use-macros
    [dommy.macros :only [node sel1]]))

(defn- add-row [tbl col-num row-idx]
  (let [row (node [:tr])]
    (dommy/append! tbl row)
    (dotimes [col-idx col-num]
      (dommy/append! row (node [:td {:class (str row-idx col-idx)}])))))

(defn create-board [size]
    (dotimes [r size]
      (add-row (sel1 :.board) size r)))

(defn- update-cell! [row col value]
  (dommy/set-text! (class-sel1
                    (str row col))
                   (if (not (= 0 value)) value)))

(defn update-ui! [state]
  (doseq [[x row] (map identity state)
          [y value] (map identity row)]
      (update-cell! x y value)))
