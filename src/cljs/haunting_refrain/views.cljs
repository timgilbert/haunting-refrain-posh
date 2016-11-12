(ns haunting-refrain.views
    (:require [re-frame.core :as re-frame]
              [re-com.core :as re-com]))

(defn title []
  (let [name (re-frame/subscribe [:name])]
    (fn []
      [re-com/title
       :label (str "Hello from " @name)
       :level :level1])))

(defn main-panel []
  (fn []
    [re-com/v-box
     :height "100%"
     :children [[title]]]))
