(ns haunting-refrain.core-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [haunting-refrain.core :as core]))

(deftest fake-test
  (testing "fake description"
    (is (= 1 2))))
