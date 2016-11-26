(ns haunting-refrain.datascript.playlist
  (:require [shodan.console :as console]
            [cljs-time.core :as t]
            [datascript.core :as d]
            [posh.reagent :as posh]
            [haunting-refrain.model.input :as input]))

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
                 {:db/id         (d/tempid :db.part/user)
                  :track/number  (inc index)
                  :track/checkin eid
                  :track/selected-field :foursquare/name
                  :playlist/_tracks pl-eid})
        result (d/transact! conn tracks)]
    (console/log tracks)))

(defn clear-playlist!
  "Remove all tracks from a playlist"
  [conn playlist-eid]
  (let [tracks  (d/q '[:find [?t ...]
                       :in $ ?playlist
                       :where [?playlist :playlist/tracks ?t]]
                     (d/db conn) playlist-eid)
        retract (for [tr tracks]
                  [:db/retract playlist-eid :playlist/tracks tr])]

    (d/transact! conn retract)))

(defn delete-playlist!
  "Remove a playlist and its tracks"
  [conn playlist-eid]
  (console/log "Removing playlist" playlist-eid)
  ;; This throws an error
  (d/transact! conn [[:db.fn/retractEntity playlist-eid]])
  (console/log "Removed"))

(defn select-random-field
  [db checkin-eid]
  (let [entity (d/touch (d/entity db checkin-eid))]
    (input/random-field entity)))

(defn select-input-fields!
  "For each track in the playlist, select a random field to be used as search data"
  [conn track-eids]
  (let [db     (d/db conn)
        fields (for [track (d/pull-many db '[:db/id {:track/checkin [*]}] track-eids)
                     :let [field (input/random-field (:track/checkin track))]]
                 {:db/id                (:db/id track)
                  :track/selected-field field})]
    (console/log "f" (doall fields))
    (d/transact! conn fields)))

(defn get-all-tracks [db playlist-eid]
  (d/q '[:find [?t ...]
         :in $ ?playlist
         :where [?playlist :playlist/tracks ?t]]
       db playlist-eid))

(defn shuffle-input-fields!
  [conn playlist-eid]
  (console/log "all" (get-all-tracks (d/db conn) playlist-eid))
  (select-input-fields! conn (get-all-tracks (d/db conn) playlist-eid)))

(defn playlist-rxn [conn name]
  (posh/pull conn '[:playlist/name {:playlist/tracks [*]}] [:playlist/name name]))
