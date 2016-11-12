(ns haunting-refrain.events
    (:require [re-frame.core :as re-frame]
              [haunting-refrain.db :as db]))

(re-frame/reg-event-db
 :initialize-db
 (fn  [_ _]
   db/default-db))
