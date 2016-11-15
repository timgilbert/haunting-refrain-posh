(ns haunting-refrain.datascript.core
  (:require [datascript.core :as d]
            [posh.reagent :as posh]
            [haunting-refrain.config :as config]
            [haunting-refrain.datascript.playlist :as playlist]))

(def ^:private hr-schema
  {:playlist/tracks {:db/cardinality :db.cardinality/many
                     :db/valueType :db.type/ref
                     :db/isComponent true}
   :playlist/name   {:db/unique :db.unique/identity}
   :track/checkin   {:db/valueType :db.type/ref
                     :db/isComponent true}})

(defn create-connection! []
  (let [conn (d/create-conn hr-schema)]
    (playlist/create-empty-playlist! conn config/default-playlist-name)
    (posh/posh! conn)
    conn))
