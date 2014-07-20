(ns adad.core-test
  (:require-macros [cemerick.cljs.test
                    :refer (is deftest with-test run-tests testing test-var)])
  (:require
   [cemerick.cljs.test :as test]))

(deftest first-test
  (is (= "" "x")))
