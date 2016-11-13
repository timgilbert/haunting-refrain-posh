(ns haunting-refrain.views.index
  (:require [re-frame.core :as re-frame]
            [haunting-refrain.components.misc :as misc]))

(defn go-button []
  (let [logged-in? (re-frame/subscribe [:auth/logged-in? :foursquare])]
    (fn []
      (if @logged-in?
        [misc/big-button
         {:dispatch [:foursquare/get-checkins]
          :icon "fa-foursquare"}
         "Get Checkins!"]
        [:b "Log in to get checkins"]))))

(defn index-page []
  [:div.container
   [:h1.title "Haunting Refrain"]
   [:h2.subtitle "Your Stochastic Soundtrack"]
   [:div.content
    [:p "Haunting Refrain builds a semi-random playlist out of your foursquare checkins."]
    [:p "It does not save any information about you."]
    [:p "To get started, log in to Foursquare and Spotify using the links in the top navigation."]

    ;; temp
    [go-button]]])

(defn about-page []
  [:div.container
   [:h1.title "About Haunting Refrain"]
   [:p "Haunting Refrain was assembled byte by byte by me, Tim Gilbert."]])
