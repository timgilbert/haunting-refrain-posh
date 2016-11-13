(ns haunting-refrain.fx.auth
  (:require [re-frame.core :as re-frame]
            [shodan.console :as console]))

(def ^:private foursquare-client-id
  "BAL2VGI3TXOWFI1TGH4O4VIHBLQ4AUC404YYSRRT5OJJEGGL")

(def ^:private foursquare-callback-url "http://localhost:3449/foursquare-hello")

(def ^:private spotify-client-id
  "81a750b7679d4abfbd5a10b5ec5c426b")

(def ^:private spotify-callback-url "http://localhost:3449/spotify-hello")

(def ^:private auth-services
  {:foursquare
   {:auth/token "access_token"
    :auth/url   (str "https://foursquare.com/oauth2/authenticate"
                     "?client_id=" foursquare-client-id
                     "&response_type=token"
                     "&redirect_uri=" foursquare-callback-url)}
   :spotify
   {:auth/token "access_token"
    :auth/url   (str "https://accounts.spotify.com/authorize"
                     "?client_id=" spotify-client-id
                     "&response_type=token"
                     "&scopes=playlist-modify-public%2playlist-modify-private"
                     "&redirect_uri=" spotify-callback-url)}})

(re-frame/reg-sub
  :auth/logged-in?
  (fn [db [_ service]]
    (some? (get-in db [:auth/access-token service]))))

(re-frame/reg-event-fx
  :auth/login
  (fn [_ [_ service]]
    (if-let [url (get-in auth-services [service :auth/url])]
      {:navigate [url :redirect]}
      (console/error "Can't find an authorization url for" service))))

(re-frame/reg-event-db
  :auth/logout
  (fn [db [_ service]]
    (update-in db [:auth/access-token] dissoc service)))

(re-frame/reg-event-fx
  :auth/parse-token
  [(re-frame/inject-cofx :url-fragment)]
  (fn [cofx [_ service]]
    (let [token-name (get-in auth-services [service :auth/token])
          token      (get-in cofx [:url-fragment token-name])]
      (console/log "t" token "tn" token-name)
      {:db       (assoc-in (:db cofx) [:auth/access-token service] token)
       :dispatch [:navigate/replace :main/index]})))
