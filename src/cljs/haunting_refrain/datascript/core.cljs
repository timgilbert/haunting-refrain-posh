(ns haunting-refrain.datascript.core
  (:require [datascript.core :as d]
            [posh.reagent :as posh]
            [mount.core :as mount]
            [haunting-refrain.config :as config]
            [haunting-refrain.datascript.playlist :as playlist]
            [shodan.console :as console]))

(def ^:private hr-schema
  {:playlist/tracks     {:db/cardinality :db.cardinality/many
                         :db/valueType   :db.type/ref
                         :db/isComponent true}
   :playlist/name       {:db/unique      :db.unique/identity}
   :track/checkin       {:db/valueType   :db.type/ref
                         :db/isComponent true}
   :track/selected-song {:db/valueType   :db.type/ref
                         :db/isComponent true}
   :spotify/track       {:db/valueType   :db.type/ref}})

(defn create-db! []
  (let [conn (d/create-conn hr-schema)]
    (console/log "Created datascript connection" conn)
    conn))

(defn reset-db! [conn]
  (d/reset-conn! conn (d/empty-db hr-schema)))

(mount/defstate datascript-conn :start (create-db!))

(defn initialize-connection!
  "Create a new datascript connection, set up some intial data in the database, and return a
  map including the connection which will be merged into the initial app-db."
  []
  (let [playlist-eid (playlist/create-empty-playlist! @datascript-conn config/default-playlist-name)]
    (posh/posh! @datascript-conn)
    {:ds/conn     @datascript-conn
     :ds/playlist playlist-eid}))
