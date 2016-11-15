(ns haunting-refrain.fx.playlist
  (:require [re-frame.core :refer [reg-event-fx reg-event-db]]
            [haunting-refrain.datascript.playlist :as ds]
            [shodan.console :as console]
            [datascript.core :as d]))

;; Subscription for the big list of checkins that forms a playlist

(defn generate-random-playlist [db]
  (console/log "Generating random list!")
  (let [pl (ds/select-random-checkins (d/db (:datascript db)))]
    (console/log pl))
  db
  )

(reg-event-db :playlist/generate-random generate-random-playlist)
