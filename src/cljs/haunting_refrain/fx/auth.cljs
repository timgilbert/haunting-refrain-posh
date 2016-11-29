(ns haunting-refrain.fx.auth
  (:require [re-frame.core :refer [reg-event-fx reg-cofx reg-sub inject-cofx]]
            [shodan.console :as console]
            [cemerick.url :as url]
            [clojure.string :as string]))

(def ^:private foursquare-client-id
  "BAL2VGI3TXOWFI1TGH4O4VIHBLQ4AUC404YYSRRT5OJJEGGL")

(def ^:private spotify-client-id
  "81a750b7679d4abfbd5a10b5ec5c426b")

(def ^:private auth-services
  "These maps set up some URLs and other things for external sites that support
  OAuth 2.0-ish access to their APIs. Note that in the URLs below, the string XXX
  will be replaced by the origin of the current URL the browser is on (scheme, host
  and port)."
  {:foursquare
   {:auth/token "access_token"
    :auth/url   (str "https://foursquare.com/oauth2/authenticate"
                     "?client_id=" foursquare-client-id
                     "&response_type=token"
                     "&redirect_uri="
                     "XXX/foursquare-hello")}
   :spotify
   {:auth/token "access_token"
    :auth/url   (str "https://accounts.spotify.com/authorize"
                     "?client_id=" spotify-client-id
                     "&response_type=token"
                     "&scopes=playlist-modify-public%2playlist-modify-private"
                     "&redirect_uri="
                     "XXX/spotify-hello")}})

(defn url-fragment-coeffect
  "The coeffect will examine the current page's URL and treat the fragment (part
  after the # sign) as a URL query string, decode it to a map, and assoc it with
  the :url-fragment coeffect."
  [coeffects _]
  (let [frag-map (-> (.. js/window -location -href)
                     (url/url)
                     :anchor
                     (url/query->map))]
    (assoc coeffects :hr/url-fragment frag-map)))

(reg-cofx :hr/url-fragment url-fragment-coeffect)

(reg-sub
  :auth/logged-in?
  (fn [db [_ service]]
    (some? (get-in db [:auth/access-token service]))))

(reg-event-fx
  :auth/login
  [(inject-cofx :hr/origin)]
  (fn [{:keys [hr/origin]} [_ service]]
    (if-let [base-url (get-in auth-services [service :auth/url])]
      (let [full-url (string/replace base-url #"XXX" origin)]
        {:hr/navigate [full-url :redirect]})
      (console/error "Can't find an authorization url for" service))))

(reg-event-fx
  :auth/logout
  (fn [{:keys [db]} [_ service]]
    (let [new-db (update-in db [:auth/access-token] dissoc service)]
      {:db          new-db
       :hr/persist! [:hr-persistance (select-keys new-db [:auth/access-token])]})))

(reg-event-fx
  :auth/parse-token
  [(inject-cofx :hr/url-fragment)]
  (fn [cofx [_ service]]
    (let [token-name (get-in auth-services [service :auth/token])
          token      (get-in cofx [:hr/url-fragment token-name])
          new-db     (assoc-in (:db cofx) [:auth/access-token service] token)]
      (console/log "t" token "tn" token-name)
      {:db          new-db
       :hr/persist! [:hr-persistance (select-keys new-db [:auth/access-token])]
       :dispatch    [:navigate/replace :main/index]})))
