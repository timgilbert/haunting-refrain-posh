(ns haunting-refrain.views.shell
  (:require [re-frame.core :as re-frame]
            [re-com.core :as re-com]
            [haunting-refrain.route.table :as table]))

(defn- oops [page]
  [:h3 (str "oops! page not found for " page)])

(defn- child-page []
  (let [current-page (re-frame/subscribe [:route/current-page])]
    (fn []
      (let [page (table/page-for @current-page)]
        (if (nil? page)
          [oops @current-page]
          [page])))))

(defn shell []
  (fn []
    [re-com/v-box
     :height "100%"
     :children [[child-page]]]))
