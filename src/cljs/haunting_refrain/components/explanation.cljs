(ns haunting-refrain.components.explanation
  (:require [haunting-refrain.util :as u]))

(defmulti explanation (fn [track] (:selected track)))

(defmethod explanation :foursquare/name
  [{:keys [venue date datum]}]
  [:div.reason
   "On " [:strong date] ", "
   "you checked in to a place called "
   [:strong venue] "."])

(defmethod explanation :foursquare/event
  [{:keys [venue date datum]}]
  [:div.reason
   "On " [:strong date] ", "
   "you checked in to " [:span.venue venue] " "
   "for an event called " [:strong datum] "."])

(defmethod explanation :foursquare/event-type
  [{:keys [venue date datum]}]
  [:div.reason
   "On " [:strong date] ", "
   "you checked in to " [:span.venue venue] " "
   "for a " [:strong datum] "type of event."])

(defmethod explanation :foursquare/category
  [{:keys [venue date datum]}]
  [:div.reason
   "On " [:strong date] ", "
   "you checked in to " [:span.venue venue] ", "
   "which is a " [:strong datum] "."])

(defmethod explanation :foursquare/address
  [{:keys [venue date datum]}]
  [:div.reason
   "On " [:strong date] ", "
   "you checked in to " [:span.venue venue] ", "
   "located at " [:strong datum] "."])

(defmethod explanation :foursquare/cross
  [{:keys [venue date datum]}]
  [:div.reason
   "On " [:strong date] ", "
   "you checked in to " [:span.venue venue] ", "
   "located at " [:strong datum] "."])

(defmethod explanation :foursquare/city
  [{:keys [venue date datum]}]
  [:div.reason
   "On " [:strong date] ", "
   "you checked in to " [:span.venue venue] " "
   "in the city of " [:strong datum] "."])

(defmethod explanation :foursquare/state
  [{:keys [venue date datum]}]
  [:div.reason
   "On " [:strong date] ", "
   "you checked in to " [:span.venue venue] " "
   "in the state of " [:strong datum] "."])

(defmethod explanation :foursquare/country
  [{:keys [venue date datum]}]
  [:div.reason
   "On " [:strong date] ", "
   "you checked in to " [:span.venue venue] " "
   "in the country of " [:strong datum] "."])

(defmethod explanation :foursquare/shout
  [{:keys [venue date datum]}]
  [:div.reason
   "On " [:strong date] ", "
   "you checked in to " [:span.venue venue] " "
   "and commented, &quot;" [:strong datum] "&quot;."])

(defn reason [track]
  (let [checkin  (:track/checkin track)
        selected (:track/selected-field track)]
    (explanation {:selected selected
                  :date     (u/format-time (:foursquare/date checkin))
                  :venue    (:foursquare/name checkin)
                  :datum    (get checkin selected)})))
