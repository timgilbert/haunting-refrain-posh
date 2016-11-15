(ns haunting-refrain.datascript.playlist
  (:require [shodan.console :as console]
            [cljs-time.core :as t]
            [datascript.core :as d]
            [posh.reagent :as posh]))

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

(defn create-empty-playlist!
  "Given a connection and list of eids, create a new playlist entity and a single track
  entity per check-in"
  [conn name]
  (let [tx-data [{:db/id           (d/tempid :db.part/user)
                  :playlist/name   name}]]
    (d/transact! conn tx-data)))

(defn save-playlist!
  "Given a connection and list of eids, create a new playlist entity and a single track
  entity per check-in"
  [conn name checkin-list]
  (let [tracks (for [[index eid] (map-indexed vector checkin-list)]
                 {:db/id         (d/tempid :db.part/user)
                  :track/number  (inc index)
                  :track/checkin eid})
        tx-data [{:db/id           (d/tempid :db.part/user)
                  :playlist/name   name
                  :playlist/tracks tracks}]]
    (d/transact! conn tx-data))
    )

(defn clear-playlist!
  "Remove every playlist and its tracks"
  [conn name]
  (console/log "Removing playlist" name)
  (d/transact! conn [[:db.fn/retractEntity [:playlist/name name]]]))

(defn playlist-rxn [conn name]
  (posh/pull conn '[*] [:playlist/name name]))
