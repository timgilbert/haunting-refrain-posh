(ns haunting-refrain.test.playlist
  (:require [cljs.test :refer-macros [deftest testing is use-fixtures]]
            [haunting-refrain.test.util :as test-util]
            [datascript.core :as d]
            [haunting-refrain.datascript.foursquare :as df]
            [haunting-refrain.datascript.playlist :as dp]
            [haunting-refrain.test.data :as data]))

(use-fixtures :once test-util/once)

(deftest test-main-flow
  (let [[conn playlist-eid] (test-util/init-ds)]
    (testing "Playlist exists"
      (let [p (d/q '[:find [?p ...]
                     :where [?p :playlist/name _]]
                   (d/db conn))]
        (is (= 1 (count p)))))
    (testing "No tracks"
      (let [p (d/q '[:find [?t ...]
                     :where [_ :playlist/tracks ?t]]
                   (d/db conn))]
        (is (= 0 (count p)))))
    (testing "Select random checkins"
      (df/parse-checkins! conn data/checkins)
      (doseq [size [1 10 20 data/num-checkins]]
        (let [rando (dp/select-random-checkins (d/db conn) size)]
          (is (= size (count rando)))))
      ;; Since we're sorting, two random selections of everything should be equal
      ;; ...or should it? Maybe we need to pull the foursquare ids for this to work
      #_(is (= (dp/select-random-checkins (d/db conn) data/num-checkins)
             (dp/select-random-checkins (d/db conn) data/num-checkins))))
    (testing "Select random checkins"
      (df/parse-checkins! conn data/checkins)
      (let [size  10
            rando (dp/select-random-checkins (d/db conn) size)
            _     (dp/save-playlist! conn playlist-eid rando)
            tr    (d/q '[:find [?t ...]
                         :in $ ?playlist
                         :where [?playlist :playlist/tracks ?t]]
                       (d/db conn) playlist-eid)]

        (is (= (count rando) (count tr)))))
    (testing "Clear playlist"
      (dp/clear-playlist! conn playlist-eid)
      ;; No more tracks
      (is (empty? (d/q '[:find [?t ...]
                         :where [_ :playlist/tracks ?t]])))
      ;; No more playlists
      (is (empty? (d/q '[:find [?p ...]
                         :where [?p :playlist/name _]]))))))
