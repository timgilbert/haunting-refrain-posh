(ns haunting-refrain.views.spotify
  (:require [re-frame.core :as rf]
            [haunting-refrain.components.misc :as misc]))

(defn- login-button []
  [misc/big-button
   {:dispatch [:auth/login :spotify]
    :icon "fa-spotify"}
   "Log In to Spotify"])

(defn- logout-button []
  [misc/big-button
   {:dispatch [:auth/logout :spotify]
    :icon "fa-spotify"
    :class "is-warning"}
   "Log Out of Spotify"])

(defn- logged-out-page []
  [:div.container.content
   [:h1.title "Log In to Spotify"]
   [:div
    [:p "To log in to foursquare, hit the link below. You'll be redirected to foursquare to authorize the "
     "application, after which you'll be returned to Haunting Refrain."]
    [login-button]]])

(defn- logged-in-page []
  [:div.container.content
   [:h1.title "Foursquare"]
   [:div
    [:p "Your browser has been authenticated with Spotify. To log out, use this button:"]
    [logout-button]]])

(defn spotify-page []
  (let [logged-in (rf/subscribe [:auth/logged-in? :spotify])]
    (fn []
      (if @logged-in
        [logged-in-page]
        [logged-out-page]))))

(defn hello-page
  "This page is the entry point into hr when the user returns from spotify authentication."
  []
  (rf/dispatch [:auth/parse-token :spotify])
  (fn []
    [:div.container.content ""]))
