(ns haunting-refrain.core-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [haunting-refrain.datascript.core :as ds]
            [datascript.core :as d]))

(deftest fake-test
  (testing "fake description"
    (is (= 1 1))))
