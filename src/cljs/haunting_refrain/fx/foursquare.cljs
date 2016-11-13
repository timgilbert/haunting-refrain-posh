(ns haunting-refrain.fx.foursquare
  (:require [re-frame.core :as re-frame]
            [shodan.console :as console]))

;; TODO: probably put these elsewhere
(def ^:private foursquare-api-version
  "Foursquare API version per https://developer.foursquare.com/overview/versioning"
  "20161111")

(def ^:private foursquare-client-id
  "BAL2VGI3TXOWFI1TGH4O4VIHBLQ4AUC404YYSRRT5OJJEGGL")

(def ^:private redirect-uri "http://localhost:3449/foursquare-hello")

(def ^:private foursquare-redirect-url
  (str "https://foursquare.com/oauth2/authenticate"
       "?client_id=" foursquare-client-id
       "&response_type=token"
       "&redirect_uri=" redirect-uri))

(re-frame/reg-sub
  :foursquare/logged-in?
  (fn [db _] (some? (:foursquare/access-token db))))

(re-frame/reg-event-fx
  :foursquare/login
  (fn [_ _]
    (console/log "login " foursquare-redirect-url)
    {:navigate [foursquare-redirect-url :redirect]}))

(re-frame/reg-event-db
  :foursquare/logout
  (fn [db _]
    (dissoc db :foursquare/access-token)))

(re-frame/reg-event-fx
  :foursquare/token-retrieved
  (fn [{:keys[db]} [_ token]]
    {:db       (assoc db :foursquare/access-token token)
     :dispatch [:navigate/push :main/index :replace]}))
