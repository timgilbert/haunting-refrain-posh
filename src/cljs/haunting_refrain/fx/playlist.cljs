(ns haunting-refrain.fx.playlist
  (:require [re-frame.core :refer [reg-event-fx reg-event-db reg-sub]]
            [haunting-refrain.datascript.playlist :as ds]
            [shodan.console :as console]
            [datascript.core :as d]
            [reagent.ratom :refer [reaction]]
            [posh.reagent :as posh]
            [haunting-refrain.datascript.util :as u]))

;; Subscription for the big list of checkins that forms a playlist
(defn- playlist-by-name
  [db [_ pl-name]]
  (ds/playlist-rxn (:ds/conn db) [:playlist/name pl-name]))

(reg-sub :playlist/contents playlist-by-name)


(defn generate-random-playlist [{:keys [db]} [_ pl-eid]]
  ;(console/log "Generating random list!")
  (let [conn (:ds/conn db)
        checkins (ds/select-random-checkins (d/db conn))]
    ;(console/log "ci:" checkins)
    (ds/save-playlist! conn pl-eid checkins)
    (ds/shuffle-input-fields! conn pl-eid)
    {:dispatch-n (for [eid (u/get-playlist-tracks conn pl-eid)
                       :let [track (u/pull-track-info (d/db conn) eid)]]
                   [:spotify/search-by-track track])}))

(reg-event-fx :playlist/generate-random generate-random-playlist)

(defn clear-playlist [db [_ pl-name]]
  ;(console/log "Clearing" pl-name)
  (let [conn (:ds/conn db)]
    (u/clear-playlist! conn [:playlist/name pl-name])
    db))

(reg-event-db :playlist/clear-all clear-playlist)

(defn shuffle-input [db [_ pl-name]]
  (console/log "Shuffling" pl-name)
  (let [conn (:ds/conn db)
        pl (ds/shuffle-input-fields! conn [:playlist/name pl-name])]
    (console/log pl)
    db))

(reg-event-db :playlist/shuffle-input shuffle-input)

;(defn search-by-checkin! [{:keys [db]} [_ track]]
;  (console/log "Searching" track)
;  {:dispatch [:spotify/search-by-checkin track]})
;
;(reg-event-fx :playlist/search-by-checkin search-by-checkin!)
