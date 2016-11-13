(ns haunting-refrain.fx.core
  (:require haunting-refrain.fx.auth
            haunting-refrain.fx.datascript
            haunting-refrain.fx.foursquare
            haunting-refrain.fx.http
            haunting-refrain.fx.local-storage
            haunting-refrain.fx.navigation
            [re-frame.core :refer [reg-event-fx inject-cofx]]
            [cemerick.url :as url]))

(def default-db
  {:route/current-page :main/index})

(defn- initialize
  "Main re-frame initialization. Retrieves persisted storage from the localStorage key
  :hr-persistance to set up the default database."
  [{:keys [local-storage]} [_]]
  {:db (merge default-db local-storage)})

(reg-event-fx
  :initialize-db
  [(inject-cofx :local-storage :hr-persistance)]
  initialize)
