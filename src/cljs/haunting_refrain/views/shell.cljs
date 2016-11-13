(ns haunting-refrain.views.shell
  (:require [re-frame.core :as re-frame]
            [haunting-refrain.route.table :as table]
            [haunting-refrain.views.nav :as nav]
            [haunting-refrain.components.misc :as misc]))

(defn- oops [page]
  [:div.container
   [:h3 (str "oops! page not found for " page)]])

(defn- page-content []
  (let [current-page (re-frame/subscribe [:route/current-page])]
    (fn []
      (let [page (table/page-for @current-page)]
        [:section.section.main-content
         (if (nil? page)
           [oops @current-page]
           [page])]))))

(defn shell []
  (fn []
    [:div.everything
     [nav/top-nav]
     [nav/left-nav]
     [page-content]
     [nav/footer]
      ]))
