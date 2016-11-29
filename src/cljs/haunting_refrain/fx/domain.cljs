(ns haunting-refrain.fx.domain
  (:require [re-frame.core :refer [reg-cofx]]
            [cemerick.url :as url]))

(defn- current-origin-coeffect
  "Coeffect which figures out what dns origin the app is currently running on and associates
  it with the :domain coeffect."
  [coeffects]
  (assoc coeffects :hr/origin (.. js/window -location -origin)))

(reg-cofx :hr/origin current-origin-coeffect)
