(ns haunting-refrain.fx.core
  (:require haunting-refrain.fx.foursquare
            haunting-refrain.fx.navigation
            haunting-refrain.fx.spotify
            [re-frame.core :as re-frame]))

(def default-db
  {:route/current-page :main/index})

(re-frame/reg-event-db
  :initialize-db
  (fn  [_ _]
    default-db))
