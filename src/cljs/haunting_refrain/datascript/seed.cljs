(ns haunting-refrain.datascript.seed
  (:require [shodan.console :as console]
            [datascript.core :as d]
            [haunting-refrain.model.input :as input]
            [haunting-refrain.datascript.util :as u]))

;; Every checkin has a list of fields, one of which can be selected at random to use as a
;; search string. Every randomly-selected field is a single datum, which we use to search
;; music providers for songs. We store these datums as seeds and hang a list of songs off
;; of them. When the checkin list is shuffled, we re-generate seeds for any fields we
;; haven't seen yet.

(defn generate-seed [db track-eid]
  (let [track   (d/pull db [:db/id {:track/checkin [:*]} {:track/seed [:*]}] track-eid)
        checkin (d/entity db (-> track :track/checkin :db/id))
        field   (input/random-field checkin)
        datum   (input/sanitize-datum field (get checkin field))
        extant  (d/q '[:find ?d .
                       :in $ ?datum
                       :where [?d :seed/datum ?datum]]
                     db datum)]
    (if extant
      ;; If a field already exists, just update the track to use it
      {:db/id       extant
       :track/_seed track-eid}
      ;; Otherwise create a new seed element and assign it to the track
      {:db/id        (d/tempid :db.part/user)
       :seed/checkin (:db/id checkin)
       :seed/field   field
       :seed/datum   datum
       :track/_seed  track-eid})))

(defn attach-seeds-to-playlist!
  "Given a playlist, attach a random seed to each of its tracks that doesn't already have one
  by selecting a random field from the checkin data associated with the track."
  [conn playlist-eid]
  (let [db    (d/db conn)
        tr    (u/get-playlist-tracks conn playlist-eid)
        seeds (map (partial generate-seed db) tr)]
    (d/transact! conn seeds)))
