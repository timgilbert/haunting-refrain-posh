(ns haunting-refrain.route.router
  (:require [mount.core :refer [defstate]]
            [haunting-refrain.route.table :as table]
            [pushy.core :as pushy]
            [shodan.console :as console]
            [sibiro.core :as sibiro]
            [re-frame.core :as rf]))

(defn- match
  "Try to match a URL, returning a map of {:route-handler :route-params :alternatives}."
  [url]
  (let [res (sibiro/match-uri @table/all-routes url :get)]
    (console/log "URL match:" res)
    res))

(defn- dispatch
  "Once the route has been matched, take an action on the dispatched route"
  [{:keys [route-handler route-params alternatives]}]
  (rf/dispatch [:route/changed route-handler route-params]))

(defn- start-pushy! []
  (let [history (pushy/pushy dispatch match)]
    (pushy/start! history)
    history))

(defstate pushy-instance :start (start-pushy!) :stop #(pushy/stop! @pushy-instance))

(defn href
  [route-keyword & [params]]
  (sibiro/path-for @table/all-routes route-keyword params))

(defn link
  ([route-name content]
   (link nil route-name content))
  ([attr route-name content]
   [:a (merge {:href (href route-name)} attr) content]))

