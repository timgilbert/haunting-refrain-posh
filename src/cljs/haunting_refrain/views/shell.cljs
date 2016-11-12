(ns haunting-refrain.views.shell
  (:require [re-frame.core :as re-frame]
            [re-com.core :as re-com]
            [haunting-refrain.route.table :as table]
            [haunting-refrain.components.misc :as misc]))

(defn- oops [page]
  [:h3 (str "oops! page not found for " page)])

(defn- child-page []
  (let [current-page (re-frame/subscribe [:route/current-page])]
    (fn []
      (let [page (table/page-for @current-page)]
        [re-com/box
         :size "auto"
         ;:height "100%"
         :child
         (if (nil? page)
           [oops @current-page]
           [page])]))))

(defn active-nav-link [route label]
  [:b label])

(defn- nav-link [route label]
  (let [current-page (re-frame/subscribe [:route/current-page])]
    (fn [route label]
      [re-com/box
       :size "auto"
       :justify :center
       :child
       (if (= @current-page route)
         [active-nav-link route label]
         [misc/link route label])])))

(defn- top-nav []
  (fn []
    [re-com/h-box
     :width "100%"
     :children
     [[nav-link :main/index "Home"]
      [nav-link :main/about "About"]
      [nav-link :foursquare/login "Foursquare"]]]))

(defn footer []
  (fn []
    [re-com/box
     :justify :center
     :height "20px"
     :child
     [:footer "Footer"]]))

(defn shell []
  (fn []
    [re-com/v-box
     :height "100%"
     :padding "20px"
     :children
     [[top-nav]
      [child-page]
      [footer]
      ]]))
