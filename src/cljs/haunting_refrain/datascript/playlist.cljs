(ns haunting-refrain.datascript.playlist
  (:require [shodan.console :as console]
            [cljs-time.core :as t]
            [datascript.core :as d]))

;; TODO: limit by check-in time
(defn select-random-checkins
  "Select a bunch of random checking and return their entity ids in order of newest to oldest."
  ([db]
   (select-random-checkins db 10))
  ([db size]
   (let [all (d/q '[:find ?d ?e
                    :in $
                    :where [?e :foursquare/date ?d]]
                  db)
         sorted (->> all         ; #{[date eid] [date eid] ...}
                     (shuffle)
                     (take size)
                     (sort #(> (first %1) (first %2))) ; sort by date
                     (map second))] ; extract eid
     (console/log sorted)
     sorted)))
