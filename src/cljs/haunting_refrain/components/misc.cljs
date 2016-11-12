(ns haunting-refrain.components.misc
  (:require [haunting-refrain.route.router :as router]
            [re-com.core :as re-com]
            [re-frame.core :as re-frame]))

(defn a-link [contents route-name]
  [:a {:href (router/href route-name)} contents])

(defn link [route-name label]
  [re-com/hyperlink
   :label label
   :on-click #(re-frame/dispatch [:navigate route-name])])
