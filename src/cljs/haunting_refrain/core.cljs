(ns haunting-refrain.core
    (:require [reagent.core :as reagent]
              [re-frame.core :as re-frame]
              [haunting-refrain.events]
              [haunting-refrain.subs]
              [haunting-refrain.views :as views]
              [haunting-refrain.config :as config]))


(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (re-frame/dispatch-sync [:initialize-db])
  (dev-setup)
  (mount-root))
