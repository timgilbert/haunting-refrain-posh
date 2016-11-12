(ns haunting-refrain.fx.foursquare
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
  :foursquare/logged-in?
  (fn [db _] (some? (:foursquare/access-token db))))
