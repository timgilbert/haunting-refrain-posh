(ns haunting-refrain.datascript.foursquare
  (:require [shodan.console :as console]
            [cljs-time.coerce :as c]
            [datascript.core :as d]))

(defn- checkin->db-entry [{:keys [id createdAt isMayor venue event shout] :as item}]
  (let [{:keys [address crossStreet city state country]} (:location venue)
        raw {:db/id                     (d/tempid :db.part/user)
             :foursquare/id             id
             :foursquare/date           createdAt
             :foursquare/name           (:name venue)
             :foursquare/category       (get-in venue [:categories 0 :name])
             :foursquare/address        address
             :foursquare/city           city
             :foursquare/state          state
             :foursquare/country        country
             :foursquare/shout          shout
             :foursquare/cross          crossStreet
             :foursquare/event          (:name event)
             :foursquare/event-category (get-in event [:categories 0 :name])
             :foursquare/mayor?         isMayor}]
    ;; Drop nil or false values from map (this omits {isMayor: false} which is fine)
    (into {} (filter second raw))))

(defn parse-checkins!
  "Given a bunch of checkin entries, parse them out and transact them into datascript"
  [conn checkin-response]
  (let [total   (get-in checkin-response [:response :checkins :count])
        items   (get-in checkin-response [:response :checkins :items])
        tx-data (map checkin->db-entry items)
        result  (d/transact! conn tx-data)]
    (console/log "Parsed" (count items) "check-ins")))
