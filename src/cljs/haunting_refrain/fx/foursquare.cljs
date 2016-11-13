(ns haunting-refrain.fx.foursquare
  (:require [hodgepodge.core :as hp]
            [re-frame.core :as re-frame]
            [shodan.console :as console]))

(def ^:private foursquare-api-version "20161111")

(defn- foursquare-api
  "Return the route to a foursquare API endpoint"
  [endpoint token]
  (str "https://api.foursquare.com/v2/"
       endpoint
       "?oauth_token=" token
       "&v=" foursquare-api-version))

(defn get-checkins
  "Persist an object into localStorage at the given key."
  [{:keys [db]} [_]]
  (let [token (get-in db [:auth/access-token :foursquare])]
    {:dispatch [:http/request
                {:method     :get
                 :endpoint   :foursquare/get-checkins
                 :url        (foursquare-api "users/self/checkins" token)
                 :on-success :foursquare/get-checkins-success
                 :on-failure :foursquare/get-checkins-failure}]}))

(re-frame/reg-event-fx :foursquare/get-checkins get-checkins)

(re-frame/reg-event-db
  :foursquare/get-checkins-failure
  (fn [db [_ body status]]
    (console/warn "Oh noes! Checkin attempt returned " status ", body:" body)
    db))

(re-frame/reg-event-db
  :foursquare/get-checkins-success
  (fn [db [_ body]]
    (console/log "Woo hoo, success!")
    (console/log body)
    db))


