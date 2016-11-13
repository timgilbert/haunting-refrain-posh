(ns haunting-refrain.views.foursquare
  (:require [re-frame.core :as re-frame]))

(defn login-button []
  [:div.columns
  [:div.column.is-half.is-offset-one-quarter
  [:a.button.is-primary.is-medium {:on-click #(re-frame/dispatch [:foursquare/login])}
   [:span.icon
    [:i.fa.fa-foursquare]]
   [:span "Log In to Foursquare"]]]])

(defn login-page []
  [:div.container.content
   [:h1.title "Log In to Foursquare"]
   [:div
    [:p "To log in to foursquare, hit the link below. You'll be redirected to foursquare to authorize the "
        "application, after which you'll be returned to Haunting Refrain."]
    [login-button]]])
