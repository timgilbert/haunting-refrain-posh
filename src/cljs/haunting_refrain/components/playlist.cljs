(ns haunting-refrain.components.playlist
  (:require [re-frame.core :as rf]
            [haunting-refrain.util :as u]
            [shodan.console :as console]))

(defn- dice [params]
  [:a.card-header-icon
   [:i.fa.fa-refresh]])

(defn single-track [track]
  (let [checkin (:track/checkin track)]
    [:div.card.is-fullwidth
     [:header.card-header
      [:p.card-header-title
       (str "Track " (:track/number track))]
      [dice ]]
     [:div.card-content
      [:div.content
      [:p (u/format-time (:foursquare/date checkin))
       [:br]
       (:foursquare/name checkin)]]]]))

(defn- empty-track-list []
  [:p "no tracks yet"])

(defn track-list [tracks]
  (if (empty? tracks)
    [empty-track-list]
    [:div.columns
     [:div.column
      (for [t tracks]
        ^{:key (:db/id t)}
        [single-track t])]]))

(defn existing-playlist [rxn]
  ;; No idea why I need to deref twice here
  (let [hmm rxn]
    (fn [rxn]
      (let [data @rxn]
        [:section.section
         [:h1.title "Playlist " (:playlist/name data)]
         [track-list (:playlist/tracks data)]]))))

(defn playlist-display [name]
  (let [sub (rf/subscribe [:playlist/contents name])]
    (fn [name]
      (let [data @sub]
        (if (nil? data)
          [:h2 "Nil data"]
          [existing-playlist data])))))
