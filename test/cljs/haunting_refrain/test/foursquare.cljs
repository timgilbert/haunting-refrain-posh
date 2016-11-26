(ns haunting-refrain.test.foursquare
  (:require [cljs.test :refer-macros [deftest testing is use-fixtures]]
            [haunting-refrain.test.util :as test-util]
            [haunting-refrain.test.data :as data]
            [haunting-refrain.datascript.foursquare :as df]
            [datascript.core :as d]))

(use-fixtures :once test-util/once)
(use-fixtures :each test-util/each)

(deftest test-parse-checkins
  (let [[conn pl] (test-util/init-ds)]
    (testing "Parse checkins"
      (df/parse-checkins! conn data/checkins)
      (let [checkin-entries (d/q '[:find [?c ...]
                                   :where [?c :foursquare/id _]]
                                 (d/db conn))]
        (is (= data/num-checkins
               (count checkin-entries)))))))
