(ns adad-utils)

(defn class-sel1 [style-class]
  (.item
   (.getElementsByClassName js/document style-class) 0))
