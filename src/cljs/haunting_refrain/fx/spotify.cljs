(ns haunting-refrain.fx.spotify
  (:require [re-frame.core :refer [reg-event-fx reg-event-db]]
            [haunting-refrain.datascript.spotify :as sp]
            [cemerick.url :as url]
            [shodan.console :as console]))

;; https://developer.spotify.com/web-api/search-item/
(defn- spotify-search-url
  "Return the route to a foursquare API endpoint"
  [search & [search-type]]
  (let [base (url/url "https://api.spotify.com/v1/search")
        full (assoc base :query {:q search :type (or search-type "track")})]
    (str full)))

(defn- query-from-checkin
  [track]
  (get-in track [:track/checkin (:track/selected-field track)]))

(defn search-by-checkin
  "Persist an object into localStorage at the given key."
  [_ [_ track]]
  (let [search (query-from-checkin track)]
    {:dispatch [:http/request
                {:method     :get
                 :endpoint   :spotify/search-by-checkin
                 :url        (spotify-search-url search)
                 :on-success [:spotify/search-success search track]
                 :on-failure [:spotify/search-failure search track]}]}))

(reg-event-fx :spotify/search-by-checkin search-by-checkin)

(reg-event-db
  :spotify/search-failure
  (fn [db [_ search track body status]]
    (console/warn "Oh noes! Spotify search for" search " returned " status ", body:" body)
    db))

(reg-event-fx
  :spotify/search-success
  (fn [{:keys [db]} [_ search track body]]
    (console/log "Search for" (query-from-checkin track) "found"
                 (get-in body [:tracks :total]) "tracks!")
    (sp/parse-songs! (:ds/conn db) search track body)
    (sp/select-random-song! (:ds/conn db) track)
    {:db db}))
