(ns haunting-refrain.components.playlist
  (:require [re-frame.core :as rf]
            [haunting-refrain.util :as u]
            [shodan.console :as console]))

(defn single-track [track]
  (let [checkin (:track/checkin track)]
    [:div
     [:p (u/format-time (:foursquare/date checkin))]
     [:p (:foursquare/name checkin)]]))

(defn track-list [tracks]
  (if (empty? tracks)
    [:p "no tracks yet"]
    [:div
     (console/log "tracks" tracks)
     [:ol
     (for [t tracks]
       ^{:key (:db/id t)}
       [:li
        [single-track t]])]]))

(defn existing-playlist [rxn]
  (let [hmm rxn]
    (fn [rxn]
      (let [data @hmm]
        [:section.section
         [:h1 "Playlist " (:playlist/name data)]
         [track-list (:playlist/tracks data)]]
        ))
    ))

(defn playlist-display [name]
  (let [sub (rf/subscribe [:playlist/contents name])]
    (fn [name]
      (let [data @sub]
        ;; No idea why I need to deref twice here
        (if (nil? data)
          [:h2 "Nil data"]
          [existing-playlist data])))))
