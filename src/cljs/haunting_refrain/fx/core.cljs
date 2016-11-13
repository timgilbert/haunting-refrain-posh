(ns haunting-refrain.fx.core
  (:require haunting-refrain.fx.auth
            haunting-refrain.fx.local-storage
            haunting-refrain.fx.navigation
            [re-frame.core :as re-frame]
            [cemerick.url :as url]))

(def default-db
  {:route/current-page :main/index})

(defn- initialize
  "Main re-frame initialization. Retrieves persisted storage from the localStorage key
  :hr-persistance to set up the default database."
  [{:keys [local-storage]} [_]]
  {:db (merge default-db local-storage)})

(re-frame/reg-event-fx
  :initialize-db
  [(re-frame/inject-cofx :local-storage :hr-persistance)]
  initialize)
