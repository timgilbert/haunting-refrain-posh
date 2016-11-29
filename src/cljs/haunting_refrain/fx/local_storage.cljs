(ns haunting-refrain.fx.local-storage
  (:require [hodgepodge.core :as hp]
            [re-frame.core :refer [reg-cofx reg-fx]]))

(defn local-storage-coeffect
  "Coeffect which retrieves an object out of localStorage under the given key and associates
  it with the :local-storage coeffect."
  [coeffects key]
  (assoc coeffects :hr/local-storage (get hp/local-storage key)))
(reg-cofx :hr/local-storage local-storage-coeffect)

(defn persist-effect
  "Persist an object into localStorage at the given key."
  [[key value]]
  (assoc! hp/local-storage key value))
(reg-fx :hr/persist! persist-effect)
