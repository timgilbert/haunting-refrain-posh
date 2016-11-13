(ns haunting-refrain.fx.navigation
  (:require [re-frame.core :as re-frame]
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

(re-frame/reg-fx :navigate navigate)

(re-frame/reg-event-fx
  :navigate/push
  (fn [_ [_ place & [params]]]
    (console/log "place" place "params" params)
    {:navigate [(router/href place params) :push]}))

(re-frame/reg-event-fx
  :navigate/replace
  (fn [_ [_ place & [params]]]
    (console/log "place" place "params" params)
    {:navigate [(router/href place params) :replace]}))
