(ns haunting-refrain.fx.domain
  (:require [re-frame.core :refer [reg-cofx reg-fx]]
            [cemerick.url :as url]))

(defn- current-origin-coeffect
  "Coeffect which figures out what dns origin the app is currently running on and associates
  it with the :domain coeffect."
  [coeffects]
  (assoc coeffects :origin (.. js/window -location -origin)))

(reg-cofx :origin current-origin-coeffect)
