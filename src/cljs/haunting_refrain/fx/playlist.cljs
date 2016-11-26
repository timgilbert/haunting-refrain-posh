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
  (let [x (ds/playlist-rxn (:ds/conn db) name)]
    (console/log x)
    x)
  )

(reg-sub :playlist/contents playlist-by-name)


(defn generate-random-playlist [db [_ pl-eid]]
  (console/log "Generating random list!")
  (let [conn     (:ds/conn db)
        checkins (ds/select-random-checkins (d/db conn))]
    (console/log "ci:" checkins)
    ;(ds/clear-playlist! conn name)
    (ds/save-playlist! conn pl-eid checkins))
  db
  )

(reg-event-db :playlist/generate-random generate-random-playlist)

(defn clear-playlist [db [_ pl-eid]]
  (console/log "Clearing" pl-eid)
  (let [conn (:ds/conn db)
        pl (ds/clear-playlist! conn pl-eid)]
    (console/log pl)
    db
    ))

(reg-event-db :playlist/clear-all clear-playlist)
