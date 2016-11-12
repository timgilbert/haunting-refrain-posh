(ns haunting-refrain.fx.spotify
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
  :spotify/logged-in?
  (fn [db _] (some? (:spotify/access-token db))))
