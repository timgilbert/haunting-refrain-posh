(ns haunting-refrain.fx.navigation
  (:require [re-frame.core :as re-frame]
            [pushy.core :as pushy]
            [haunting-refrain.route.router :as router]))

(defn hard-redirect! [history place]
  (pushy/stop! history)
  (set! (.-location js/window) place))

(defn navigate
  [[place & [type]]]
  (let [nav-fn (if (= type :redirect)
                 hard-redirect!
                 pushy/set-token!)]
    (nav-fn @router/pushy-instance place)))

(re-frame/reg-fx :navigate navigate)

(re-frame/reg-event-fx
  :navigate
  (fn [cofx [_ place & [params]]]
    {:navigate [(router/href place params)]}))
