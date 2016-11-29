(ns haunting-refrain.fx.core
  (:require haunting-refrain.fx.auth
            haunting-refrain.fx.datascript
            haunting-refrain.fx.domain
            haunting-refrain.fx.foursquare
            haunting-refrain.fx.http
            haunting-refrain.fx.local-storage
            haunting-refrain.fx.navigation
            haunting-refrain.fx.playlist
            haunting-refrain.fx.spotify
            [haunting-refrain.datascript.core :as datascript]
            [re-frame.core :refer [reg-event-fx inject-cofx]]
            [cemerick.url :as url]))

(def default-db
  {:route/current-page :main/index})

(defn- initialize-db
  "Main re-frame initialization. Retrieves persisted storage from the localStorage key
  :hr-persistance to set up the default database."
  [{:keys [hr/local-storage]} [_]]
  (let [ds-map (datascript/initialize!)]
    {:db (-> default-db
             (merge local-storage ds-map))}))

(reg-event-fx
  :initialize-db
  [(inject-cofx :hr/local-storage :hr-persistance)]
  initialize-db)
