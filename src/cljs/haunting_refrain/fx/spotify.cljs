(ns haunting-refrain.fx.spotify
  (:require [re-frame.core :as re-frame]
            [shodan.console :as console]))

(re-frame/reg-sub
  :spotify/logged-in?
  (fn [db _] (some? (:spotify/access-token db))))

;; TODO: probably put these elsewhere
(def ^:private foursquare-api-version
  "Foursquare API version per https://developer.foursquare.com/overview/versioning"
  "20161111")

(def ^:private spotify-client-id
  "81a750b7679d4abfbd5a10b5ec5c426b")

(def ^:private redirect-uri "http://localhost:3449/spotify-hello")

(def ^:private spotify-redirect-url
  (str "https://accounts.spotify.com/authorize"
       "?client_id=" spotify-client-id
       "&response_type=token"
       "&scopes=playlist-modify-public%2playlist-modify-private"
       "&redirect_uri=" redirect-uri))

(re-frame/reg-sub
  :spotify/logged-in?
  (fn [db _] (some? (:spotify/access-token db))))

(re-frame/reg-event-fx
  :spotify/login
  (fn [_ _]
    (console/log "login " spotify-redirect-url)
    {:navigate [spotify-redirect-url :redirect]}))

(re-frame/reg-event-db
  :spotify/logout
  (fn [db _]
    (dissoc db :foursquare/access-token)))

(re-frame/reg-event-fx
  :foursquare/token-retrieved
  (fn [{:keys[db]} [_ token]]
    {:db       (assoc db :foursquare/access-token token)
     :dispatch [:navigate/push :main/index :replace]}))
