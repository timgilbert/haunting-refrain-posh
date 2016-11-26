(ns haunting-refrain.test.util
  (:require [mount.core :as mount]
            [haunting-refrain.datascript.core :as ds]))

(defn start-mount []
  (enable-console-print!)
  (println "Starting mount...")
  (mount/start))

(defn reset-ds! []
  (ds/reset-db! @ds/datascript-conn))

(def once
  {:before start-mount})

(def each
  {:before reset-ds!})

(defn init-ds []
  (when (nil? ds/datascript-conn)
    (throw (ex-data "ds/datascript-conn is nil! /Did you call mount/start?")))
  (let [{:keys [:ds/conn :ds/playlist]} (ds/initialize-connection!)]
    [conn playlist]))
