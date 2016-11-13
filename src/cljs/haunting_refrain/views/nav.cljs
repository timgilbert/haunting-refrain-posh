(ns haunting-refrain.views.nav
  (:require [re-frame.core :as re-frame]
            [haunting-refrain.components.misc :as misc]))

(defn- nav-link [route label]
  (let [current-page (re-frame/subscribe [:route/current-page])]
    (fn [route label]
      (let [class (if (= @current-page route) "nav-item is-active" "nav-item")]
        [misc/link {:class class} route label]))))

(defn- service-icon
  [logged-in-sub]
  (let [logged-in? (re-frame/subscribe [logged-in-sub])]
    ))

(defn logged-out-foursquare-icon []
  [misc/link {:class "nav-item"} :foursquare/login
   [:span.icon
    [:i.fa.fa-foursquare]]])

(defn logged-in-foursquare-icon []
  [misc/link {:class "nav-item"} :foursquare/login
   [:span.icon
    [:i.fa.fa-foursquare]]])

(defn- foursquare-icon []
  (let [logged-in (re-frame/subscribe [:foursquare/logged-in?])]
    (fn []
      (if @logged-in
        [logged-out-foursquare-icon]
        [logged-in-foursquare-icon]))))

(defn- spotify-icon []
  (let [logged-in (re-frame/subscribe [:spotify/logged-in?])]
    (fn []
      [:a.nav-item
       [:span.icon.nav-item
        [:i.fa.fa-spotify]]])))

(defn- top-nav []
  (fn []
    [:section.hero.is-primary
     [:nav.nav.has-shadow
      [:div.nav-left
       [nav-link :main/index "Haunting Refrain"]]
      [:div.nav-center
       [:a.nav-item {:href "https://github.com/timgilbert"}
        [:span.icon [:i {:class "fa fa-github"}]]]]
      [:div.nav-right
       [foursquare-icon]
       [spotify-icon]
       [nav-link :main/about "About"]]]]))

(defn left-nav []
  [:div])

(defn footer []
  (fn []
    [:footer.footer
     [:div.container
      [:div.content.has-text-centered
       [:p "Haunting refain, blah footer blah blah"]]]]))

