(ns haunting-refrain.fx.auth
  (:require [re-frame.core :refer [reg-event-fx reg-cofx reg-sub inject-cofx]]
            [shodan.console :as console]
            [cemerick.url :as url]))

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

(defn url-fragment-coeffect
  "The coeffect will examine the current page's URL and treat the "
  [coeffects _]
  (let [frag-map (-> (.. js/window -location -href)
                     (url/url)
                     :anchor
                     (url/query->map))]
    (assoc coeffects :url-fragment frag-map)))

(reg-cofx :url-fragment url-fragment-coeffect)

(reg-sub
  :auth/logged-in?
  (fn [db [_ service]]
    (some? (get-in db [:auth/access-token service]))))

(reg-event-fx
  :auth/login
  (fn [_ [_ service]]
    (if-let [url (get-in auth-services [service :auth/url])]
      {:navigate [url :redirect]}
      (console/error "Can't find an authorization url for" service))))

(reg-event-fx
  :auth/logout
  (fn [{:keys [db]} [_ service]]
    (let [new-db (update-in db [:auth/access-token] dissoc service)]
      {:db       new-db
       :persist! [:hr-persistance (select-keys new-db [:auth/access-token])]})))

(reg-event-fx
  :auth/parse-token
  [(inject-cofx :url-fragment)]
  (fn [cofx [_ service]]
    (let [token-name (get-in auth-services [service :auth/token])
          token      (get-in cofx [:url-fragment token-name])
          new-db     (assoc-in (:db cofx) [:auth/access-token service] token)]
      (console/log "t" token "tn" token-name)
      {:db       new-db
       :persist! [:hr-persistance (select-keys new-db [:auth/access-token])]
       :dispatch [:navigate/replace :main/index]})))
