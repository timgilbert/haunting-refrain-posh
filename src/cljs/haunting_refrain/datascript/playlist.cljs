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
  "Create a new blank playlist, returning its eid"
  [conn name]
  (let [pl-id   (d/tempid :db.part/user)
        tx-data [{:db/id           pl-id
                  :playlist/name   name}]
         tx (d/transact! conn tx-data)]
    ;(console/log "tx" (pr-str tx))
    (-> tx :tempids (get pl-id))))

(defn save-playlist!
  "Given a connection and list of eids, create a new playlist entity and a single track
  entity per check-in"
  [conn pl-eid checkin-list]
  (let [tracks (for [[index eid] (map-indexed vector checkin-list)]
                 (do (console/log (inc index) eid)
                 {:db/id         (d/tempid :db.part/user)
                  :track/number  (inc index)
                  :track/checkin eid
                  :playlist/_tracks pl-eid}))
        result (d/transact! conn tracks)]
    (console/log tracks)
    ))

(defn clear-playlist!
  "Remove a playlist and its tracks"
  [conn playlist-eid]
  (console/log "Removing playlist" playlist-eid)
  ;; This throws an error
  (d/transact! conn [[:db.fn/retractEntity playlist-eid]])
  (console/log "Removed"))

(defn playlist-rxn [conn name]
  (posh/pull conn '[:playlist/name {:playlist/tracks [:*]}] [:playlist/name name]))

(defn playlist-yo [conn name]
  (d/pull (d/db conn) '[:playlist/name {:playlist/tracks [:*]}] [:playlist/name name]))
