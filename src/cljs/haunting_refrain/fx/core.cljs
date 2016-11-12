(ns haunting-refrain.fx.core
  (:require haunting-refrain.fx.navigation
            [re-frame.core :as re-frame]))

(def default-db
  {:route/current-page :main/index})

(re-frame/reg-event-db
  :initialize-db
  (fn  [_ _]
    default-db))
