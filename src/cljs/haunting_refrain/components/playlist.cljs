(ns haunting-refrain.components.playlist
  (:require [re-frame.core :as rf]
            [haunting-refrain.util :as u]
            [shodan.console :as console]
            [haunting-refrain.model.input :as input]
            [haunting-refrain.components.explanation :as explanation]))

(defn- dice [track]
  [:a.card-header-icon
   {:on-click #(rf/dispatch [:spotify/search-by-track track])}
   [:i.fa.fa-refresh]])

(defn song-details [track]
  (let [{:keys [:spotify/track-name :spotify/artist-name
                :spotify/album-name :spotify/album-thumb]} (:track/selected-song track)]
    [:div.media
     [:div.media-left
      [:figure.image.is-128x128
       [:img {:src album-thumb}]]]
     [:div.media-content
      [:p.title.is-5
       track-name]
      [:p.title.is-6
       artist-name " / " album-name]
      [explanation/reason track]
      ]]))

(defn song-not-found [track]
  [:div.media
   [:div.media-left
    [:figure.image.is-128x128
     [:img ]]]
   [:div.media-content
    [explanation/reason track]]])

(defn single-track [track]
  [:div.card.is-fullwidth
   [:header.card-header
    [:p.card-header-title
     (str "Track " (:track/number track))]
    [dice track]]
   [:div.card-content
    [:div.content
     (if (:track/selected-song track)
       [song-details track]
       [song-not-found track])]]])

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
