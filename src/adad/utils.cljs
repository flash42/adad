(ns adad.utils)

(defn wait [ms func]
  (js* "setTimeout(~{func}, ~{ms})"))

(defn class-sel1 [style-class]
  (.item
   (.getElementsByClassName js/document style-class) 0))
