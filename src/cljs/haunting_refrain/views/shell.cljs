(ns haunting-refrain.views.shell
  (:require [re-frame.core :as rf]
            [haunting-refrain.route.table :as table]
            [haunting-refrain.views.nav :as nav]
            [haunting-refrain.components.misc :as misc]))

(defn- oops [page]
  [:div.container
   [:h3 (str "oops! page not found for " page)]])

(defn- page-content []
  (let [current-page (rf/subscribe [:route/current-page])
        page-params  (rf/subscribe [:route/params])]
    (fn []
      (let [page (table/page-for @current-page)]
        [:section.section.main-content
         (if (nil? page)
           [oops @current-page @page-params]
           [page @page-params])]))))

(defn shell []
  (fn []
    [:div.everything
     [nav/top-nav]
     [nav/left-nav]
     [page-content]
     [nav/footer]]))
