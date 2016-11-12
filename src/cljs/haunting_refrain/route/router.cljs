(ns haunting-refrain.route.router
  (:require [mount.core :refer [defstate]]
            [haunting-refrain.route.table :as table]
            [pushy.core :as pushy]
            [shodan.console :as console]
            [sibiro.core :as sibiro]
            [re-frame.core :as re-frame]))

(defn- match
  "Try to match a URL, returning a map of {:route-handler :route-params :alternatives}."
  [url]
  (let [res (sibiro/match-uri @table/all-routes url :get)]
    (console/log "URL match:" res)
    res))

(defn- dispatch
  "Once the route has been matched, take an action on the dispatched route"
  [{:keys [route-handler route-params alternatives]}]
  (console/log route-handler route-params)
  (re-frame/dispatch [:route/changed route-handler route-params]))

(defn- start-pushy! []
  (let [history (pushy/pushy dispatch match)]
    (pushy/start! history)
    history))

(defstate pushy-instance :start (start-pushy!) :stop #(pushy/stop! @pushy-instance))

(defn href
  [route-keyword & [params]]
  (sibiro/path-for @table/all-routes route-keyword params))

;; re-frame handlers
(defn- route-changed-handler
  [db [_ route-name route-params]]
  (assoc db :route/current-page route-name
            :route/params       route-params))

(re-frame/reg-event-db :route/changed route-changed-handler)

(re-frame/reg-sub :route/current-page (fn [db _] (:route/current-page db)))
(re-frame/reg-sub :route/params       (fn [db _] (:route/params db)))
