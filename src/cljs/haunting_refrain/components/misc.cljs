(ns haunting-refrain.components.misc
  (:require [haunting-refrain.route.router :as router]
            [re-com.core :as re-com]
            [re-frame.core :as re-frame]))

(defn link
  ([route-name content]
   (link nil route-name content))
  ([attr route-name content]
   [:a (merge {:href (router/href route-name)} attr) content]))

(defn rc-link [route-name label]
  [re-com/hyperlink
   :label label
   :on-click #(re-frame/dispatch [:navigate route-name])])
