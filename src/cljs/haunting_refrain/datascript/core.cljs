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
   :seed/checkin        {:db/valueType   :db.type/ref}
   :seed/datum          {:db/unique      :db.unique/identity}
   :track/seed          {:db/valueType   :db.type/ref
                         :db/isComponent true}
   :track/selected-song {:db/valueType   :db.type/ref
                         :db/isComponent true}
   :spotify/track       {:db/valueType   :db.type/ref}})

(defn create-db! []
  (let [conn (d/create-conn hr-schema)]
    (console/log "Created datascript connection" conn)
    (posh/posh! conn)
    conn))

(mount/defstate global-datascript-conn :start (create-db!))

(defn get-connection
  "Return the datascript connection, throwing an error if it's nil"
  []
  (if-let [conn @global-datascript-conn]
    conn
    (throw (ex-info "Oops! datascript-conn is not truthy! Did you run (mount/start)?" global-datascript-conn))))

(defn reset-db! [& [connection-to-reset]]
  (d/reset-conn! (or connection-to-reset (get-connection)) (d/empty-db hr-schema)))

(defn initialize!
  "Set up all the seed data we need for the app, returning a map of eids for relevant entities."
  [& [conn-to-initialize]]
  (let [conn (or conn-to-initialize @global-datascript-conn)
        playlist-eid (playlist/create-empty-playlist! conn config/default-playlist-name)]
    {:ds/playlist playlist-eid}))
