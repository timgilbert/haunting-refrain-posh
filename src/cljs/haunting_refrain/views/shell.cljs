(ns haunting-refrain.views.shell
  (:require [re-frame.core :as re-frame]
            [haunting-refrain.route.table :as table]
            [haunting-refrain.components.misc :as misc]))

(defn- oops [page]
  [:div.container
   [:h3 (str "oops! page not found for " page)]])

(defn- page-content []
  (let [current-page (re-frame/subscribe [:route/current-page])]
    (fn []
      (let [page (table/page-for @current-page)]
        [:section.section.main-content
         (if (nil? page)
           [oops @current-page]
           [page])]))))

(defn- nav-link [route label]
  (let [current-page (re-frame/subscribe [:route/current-page])]
    (fn [route label]
      (let [class (if (= @current-page route) "nav-item is-active" "nav-item")]
         [misc/link {:class class} route label]))))

(defn foursquare-icon []
  (let [logged-in (re-frame/subscribe [:foursquare/logged-in?])]
    (fn []
      [:a.nav-item
       [:span.icon
        [:i.fa.fa-foursquare]]])))

(defn spotify-icon []
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

(defn shell []
  (fn []
    [:div.everything
     [top-nav]
     [left-nav]
     [page-content]
     [footer]
      ]))
