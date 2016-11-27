(ns haunting-refrain.datascript.util
  (:require [datascript.core :as d]
            [shodan.console :as console]
            [haunting-refrain.model.input :as input]))

(defn get-playlist-tracks
  "Given a database or connection object and an eid or lookup ref for a playlist, return a
  list of that playlist's entity ids."
  [db-or-conn playlist-eid]
  (let [db (cond (d/conn? db-or-conn) (d/db db-or-conn)
                 (d/db? db-or-conn)   db-or-conn
                 :default             (throw (ex-info "Whoa, what is db?" db-or-conn)))]
    (d/q '[:find [?t ...]
           :in $ ?playlist
           :where [?playlist :playlist/tracks ?t]]
         db playlist-eid)))

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

(defn pull-track-info
  [db track-eid]
  (d/pull db [:db/id {:track/checkin [:*]} {:track/seed [:*]}] track-eid))
