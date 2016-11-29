(ns haunting-refrain.fx.playlist
  (:require [re-frame.core :refer [reg-event-fx reg-event-db reg-sub inject-cofx subscribe]]
            [haunting-refrain.datascript.playlist :as ds]
            [shodan.console :as console]
            [datascript.core :as d]
            [reagent.ratom :refer [reaction]]
            [haunting-refrain.datascript.util :as u]))

;; TODO: figure out subs here
;; Subscription for the big list of checkins that forms a playlist
(defn- playlist-by-name
  [db [_ pl-name]]
  (ds/playlist-rxn (:ds/conn db) [:playlist/name pl-name]))

(defn- playlist-contents-input-signals
  [[_ playlist-name] _]
  (subscribe [:posh/pull
              [:playlist/name {:playlist/tracks [:*]}]
              [:playlist/name playlist-name]]))
(reg-sub
  :playlist/contents
  ;; Input signals
  playlist-contents-input-signals
  ;; Value
  (fn [posh-rxn query-v _]
    @posh-rxn))


(defn generate-random-playlist
  "Effect handler which randomizes seeds for a playlist and dispatches search events for each seed"
  [{:keys [db :ds/conn]} [_ pl-eid]]
  (let [checkins (ds/select-random-checkins (d/db conn))]
    (ds/save-playlist! conn pl-eid checkins)
    (ds/shuffle-input-fields! conn pl-eid)
    {:dispatch-n (for [eid (u/get-playlist-tracks conn pl-eid)
                       :let [track (u/pull-track-info (d/db conn) eid)]]
                   [:spotify/search-by-track track])}))

(reg-event-fx
  :playlist/generate-random
  [(inject-cofx :ds/conn)]
  generate-random-playlist)

(defn clear-playlist
  "Effect handler which clears all tracks from a playlist"
  [{:keys [db :ds/conn]} [_ pl-name]]
  (u/clear-playlist! conn [:playlist/name pl-name])
  {})

(reg-event-fx
  :playlist/clear-all
  [(inject-cofx :ds/conn)]
  clear-playlist)

(defn shuffle-input
  "Effect handler which shuffles the fields used to generate seeds from the checkins in a playlist"
  [{:keys [db :ds/conn]} [_ pl-name]]
  (ds/shuffle-input-fields! conn [:playlist/name pl-name])
  {})

(reg-event-db
  :playlist/shuffle-input
  [(inject-cofx :ds/conn)]
  shuffle-input)
