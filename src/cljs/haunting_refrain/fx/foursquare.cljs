(ns haunting-refrain.fx.foursquare
  (:require [re-frame.core :refer [reg-event-fx reg-event-db inject-cofx]]
            [haunting-refrain.datascript.foursquare :as ds]
            [shodan.console :as console]
            [haunting-refrain.config :as config]
            [haunting-refrain.datascript.util :as u]))

(def ^:private foursquare-api-version "20161111")

(defn- foursquare-api
  "Return the route to a foursquare API endpoint"
  [origin endpoint token]
  (str "https://api.foursquare.com/v2/"
       endpoint
       "?oauth_token=" token
       "&v=" foursquare-api-version))

(defn get-checkins
  "Persist an object into localStorage at the given key."
  [{:keys [db origin]} [_]]
  (let [token (get-in db [:auth/access-token :foursquare])]
    {:dispatch [:http/request
                {:method     :get
                 :endpoint   :foursquare/get-checkins
                 :url        (foursquare-api origin "users/self/checkins" token)
                 :on-success :foursquare/get-checkins-success
                 :on-failure :foursquare/get-checkins-failure}]}))

(reg-event-fx
  :foursquare/get-checkins
  [(inject-cofx :origin)]
  get-checkins)

(reg-event-db
  :foursquare/get-checkins-failure
  (fn [db [_ body status]]
    (console/warn "Oh noes! Checkin attempt returned " status ", body:" body)
    db))

(reg-event-fx
  :foursquare/get-checkins-success
  (fn [{:keys [db]} [_ body]]
    (let [{:keys [:ds/conn :ds/playlist]} db]
      (console/log "Got " (-> body :response :checkins :items count) "checkins from foursquare")
      (u/clear-playlist! conn playlist)
      (ds/parse-checkins! conn body)
      {:dispatch [:playlist/generate-random playlist]})))
