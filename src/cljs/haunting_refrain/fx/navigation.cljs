(ns haunting-refrain.fx.navigation
  (:require [re-frame.core :refer [reg-fx reg-event-db reg-event-fx reg-sub]]
            [pushy.core :as pushy]
            [shodan.console :as console]
            [haunting-refrain.route.router :as router]))

(defn hard-redirect! [history place]
  (pushy/stop! history)
  (set! (.-location js/window) place))

(defn navigate
  [[place & [type]]]
  (console/log place type)
  (let [nav-fn (case type
                 :redirect hard-redirect!
                 :replace  pushy/replace-token!
                 pushy/set-token!)]
    (nav-fn @router/pushy-instance place)))

(reg-fx :navigate navigate)

(reg-event-fx
  :navigate/push
  (fn [_ [_ place & [params]]]
    (console/log "place" place "params" params)
    {:navigate [(router/href place params) :push]}))

(reg-event-fx
  :navigate/replace
  (fn [_ [_ place & [params]]]
    (console/log "place" place "params" params)
    {:navigate [(router/href place params) :replace]}))

(defn- route-changed-handler
  "Handler called by pushy when the URL changes. route-name is the keyword name of the route;
  route-params are the (potentially nil) parameters associated with the route. See route.table
  for the big list of routes."
  [db [_ route-name route-params]]
  (assoc db :route/current-page route-name
            :route/params       route-params))

(reg-event-db :route/changed route-changed-handler)

(reg-sub :route/current-page (fn [db _] (:route/current-page db)))
(reg-sub :route/params       (fn [db _] (:route/params db)))
