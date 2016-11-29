(ns haunting-refrain.test.util
  (:require [mount.core :as mount]
            [haunting-refrain.datascript.core :as hrd]))

(defn start-mount []
  (enable-console-print!)
  (println "Starting mount...")
  (mount/start))

(defn reset-ds! []
  (hrd/reset-db! @ds/global-datascript-conn))

(def once
  {:before start-mount})

(def each
  {:before reset-ds!})

(defn init-ds []
  (let [conn (hrd/get-connection)
        {:keys [:ds/playlist]} (hrd/initialize! conn)]
    [conn playlist]))
