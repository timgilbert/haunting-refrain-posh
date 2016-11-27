(ns haunting-refrain.components.playlist
  (:require [re-frame.core :as rf]
            [haunting-refrain.util :as u]
            [shodan.console :as console]
            [haunting-refrain.model.input :as input]
            [haunting-refrain.components.explanation :as explanation]))

(defn- dice [track]
  [:a.card-header-icon
   {:on-click #(rf/dispatch [:playlist/search-by-checkin track])}
   [:i.fa.fa-refresh]])

(defn song-details [song]
  [:div.media
   [:div.media-left
    [:figure.image.is-128x128
     [:img {:src (:spotify/album-thumb song)}]]]
   [:div.media-content
    [:p.title.is-5
     (:spotify/track-name song)]
    [:p.title.is-6
     (:spotify/artist-name song)]]])

(defn single-track [track]
  (let [song (:track/selected-song track)]
    [:div.card.is-fullwidth
     [:header.card-header
      [:p.card-header-title
       (str "Track " (:track/number track))]
      [dice track]]
     [:div.card-content
      [:div.content
       [explanation/reason track]
       (when song
         [song-details song])]]]))

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
