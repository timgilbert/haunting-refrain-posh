(ns haunting-refrain.fx.playlist
  (:require [re-frame.core :refer [reg-event-fx reg-event-db reg-sub]]
            [haunting-refrain.datascript.playlist :as ds]
            [shodan.console :as console]
            [datascript.core :as d]
            [reagent.ratom :refer [reaction]]
            [posh.reagent :as posh]))

;; Subscription for the big list of checkins that forms a playlist
(defn- playlist-by-name
  [db [_ name]]
  ;(reaction (ds/playlist-rxn (:datascript @db) name))
  (ds/playlist-rxn (:datascript db) name)
  )

(reg-sub :playlist/contents playlist-by-name)


(defn generate-random-playlist [db [_ name]]
  (console/log "Generating random list!")
  (let [conn (:datascript db)
        pl (ds/select-random-checkins (d/db conn))]
    (console/log pl)
    (ds/save-playlist! conn name pl))
  db
  )

(reg-event-db :playlist/generate-random generate-random-playlist)
