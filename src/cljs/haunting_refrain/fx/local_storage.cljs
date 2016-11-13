(ns haunting-refrain.fx.local-storage
  (:require [hodgepodge.core :as hp]
            [re-frame.core :as re-frame]))

(defn local-storage-coeffect
  "Coeffect which retrieves an object out of localStorage under the given key and associates
  it with the :local-storage coeffect."
  [coeffects key]
  (assoc coeffects :local-storage (get hp/local-storage key)))
(re-frame/reg-cofx :local-storage local-storage-coeffect)

(defn persist-effect
  "Persist an object into localStorage at the given key."
  [[key value]]
  (assoc! hp/local-storage key value))
(re-frame/reg-fx :persist persist-effect)
