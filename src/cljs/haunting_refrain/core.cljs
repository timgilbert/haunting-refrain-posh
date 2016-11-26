(ns haunting-refrain.core
    (:require [reagent.core :as reagent]
              [re-frame.core :as rf]
              [haunting-refrain.fx.core]
              [haunting-refrain.views.shell :as shell]
              [haunting-refrain.config :as config]
              [re-frisk.core :as re-frisk]
              [mount.core :as mount]
              haunting-refrain.route.router))

(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (re-frisk/enable-re-frisk!)
    (println "dev mode")))

(defn mount-root []
  (reagent/render [shell/shell]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (mount/start)
  (rf/dispatch-sync [:initialize-db])
  (dev-setup)
  (mount-root))
