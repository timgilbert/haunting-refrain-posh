(ns haunting-refrain.fx.spotify
  (:require [re-frame.core :refer [reg-event-fx reg-event-db inject-cofx]]
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

(defn search-by-track
  "Persist an object into localStorage at the given key."
  [_ [_ track]]
  {:dispatch [:http/request
              {:method     :get
               :endpoint   :spotify/search-by-track
               :url        (spotify-search-url (-> track :track/seed :seed/datum))
               :on-success [:spotify/search-success track]
               :on-failure [:spotify/search-failure track]}]})

(reg-event-fx :spotify/search-by-track search-by-track)

(reg-event-db
  :spotify/search-failure
  (fn [db [_ seed body status]]
    (console/warn "Oh noes! Spotify search for" (:seed/datum seed) " returned " status ", body:" body)
    db))

(defn- spotify-search-success
  [{:keys [db :ds/conn]} [_ track body]]
  (console/log "Search for" (-> track :track/seed :seed/datum) "found"
               (get-in body [:tracks :total]) "tracks!")
  (sp/parse-songs! conn (:track/seed track) body)
  (sp/select-random-song! conn track)
  {:db db})

(reg-event-fx
  :spotify/search-success
  [(inject-cofx :ds/conn)]
  spotify-search-success)
