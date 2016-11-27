(ns haunting-refrain.datascript.spotify
  (:require [shodan.console :as console]
            [cljs-time.core :as t]
            [datascript.core :as d]
            [posh.reagent :as posh]
            [haunting-refrain.model.input :as input]))

;; https://developer.spotify.com/web-api/object-model/#track-object
(defn spotify-track->db-entry
  [seed spotify-track]
  (let [raw {:db/id               (d/tempid :db.part/user)
             :spotify/seed        (:db/id seed)
             :spotify/track-name  (:name spotify-track)
             :spotify/artist-name (get-in spotify-track [:artists 0 :name])
             :spotify/artist-id   (get-in spotify-track [:artists 0 :id])
             :spotify/album-name  (get-in spotify-track [:album :name])
             :spotify/album-id    (get-in spotify-track [:album :id])
             :spotify/album-thumb (get-in spotify-track [:album :images 1 :url])}]
    raw))

(defn parse-songs!
  [conn seed body]
  (let [items   (get-in body [:tracks :items])
        tx-data (map (partial spotify-track->db-entry seed) items)]
    (d/transact! conn tx-data)))

(defn select-random-song!
  [conn track]
  (let [track-eid (:db/id track)
        all (d/q '[:find [?s ...]
                   :in $ ?track
                   :where [?track :track/seed ?seed]
                          [?s :spotify/seed ?seed]]
                   (d/db conn) track-eid)
        rnd (when-not (empty? all)
              (rand-nth all))]
    (when rnd
      (d/transact! conn [{:db/id track-eid
                          :track/selected-song rnd}]))))
