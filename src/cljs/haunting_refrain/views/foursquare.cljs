(ns haunting-refrain.views.foursquare
  (:require [re-frame.core :as re-frame]
            [clojure.string :as string])
  (:import goog.Uri))

(defn- login-button []
  [:div.columns
  [:div.column.is-half.is-offset-one-quarter
  [:a.button.is-primary.is-medium {:on-click #(re-frame/dispatch [:foursquare/login])}
   [:span.icon
    [:i.fa.fa-foursquare]]
   [:span "Log In to Foursquare"]]]])

(defn- logout-button []
  [:div.columns
  [:div.column.is-half.is-offset-one-quarter
  [:a.button.is-warning.is-medium {:on-click #(re-frame/dispatch [:foursquare/logout])}
   [:span.icon
    [:i.fa.fa-foursquare]]
   [:span "Log Out of Foursquare"]]]])

(defn- logged-out-page []
  [:div.container.content
   [:h1.title "Log In to Foursquare"]
   [:div
    [:p "To log in to foursquare, hit the link below. You'll be redirected to foursquare to authorize the "
        "application, after which you'll be returned to Haunting Refrain."]
    [login-button]]])

(defn- logged-in-page []
  [:div.container.content
   [:h1.title "Foursquare"]
   [:div
    [:p "Your browser has been authenticated with Foursquare. To log out, use this button:"]
    [logout-button]]])

(defn foursquare-page []
  (let [logged-in (re-frame/subscribe [:foursquare/logged-in?])]
    (fn []
      (if @logged-in
        [logged-in-page]
        [logged-out-page]))))

(defn hello-page
  "This page is the entry point into hr when the user returns from foursquare authentication."
  []
  (let [uri         (Uri. (.-location js/window))
        [var token] (string/split (.getFragment uri) #"=")]
    (re-frame/dispatch [:foursquare/token-retrieved token])
    (fn []
      [:div.container.content
       "hello"])))
