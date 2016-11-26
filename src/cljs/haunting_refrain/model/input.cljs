(ns haunting-refrain.model.input
  (:require [clojure.set :as set]
            [clojure.string :as string]
            [shodan.console :as console]))

(def input-weights
  "Weighted probability list. For each field in the list"
  {:foursquare/name
   {:explanation "You checked in at a place called XXX"
    :probability 10}
   :foursquare/event
   {:explanation "You checked in to an event called XXX"
    :probability 10}
   :foursquare/event-type
   {:explanation "You checked into an XXX event"
    :probability 5}
   :foursquare/category
   {:explanation "You checked in at a XXX type of place"
    :probability 3}
   :foursquare/address
   {:explanation "You checked in at a place located at XXX"
    :probability 3}
   :foursquare/city
   {:explanation "You checked in at a place in the city XXX"
    :probability 2}
   :foursquare/state
   {:explanation "You checked in at a place in the state XXX"
    :probability 1}
   :foursquare/country
   {:explanation "You checked in at a place in the country XXX"
    :probability 1}
   :foursquare/shout
   {:explanation "You said this in your checkin: XXX"
    :probability 5}
   :foursquare/cross
   {:explanation "You checked in at a place at XXX"
    :probability 2}})

(defn explanation [input-data selected-field]
  (let [data (get input-data selected-field)
        exp  (get-in input-weights [selected-field :explanation])]
    (string/replace exp "XXX" data)))

(defn random-field
  "Inartful, inefficient implementation of weighted probability list selection. Looking
  at input-field, select one of its keys based on the input-weight field above."
  [input-data]
  (let [domain  (set/intersection (-> input-data keys set) (-> input-weights keys set))
        repeats (for [field domain
                      :let [p (get-in input-weights [field :probability])]]
                  (repeat p field))
        _ (console/log "input" (-> input-data keys set))
        _ (console/log "input" (-> input-weights keys set))
        _ (console/log "domain" domain "r" repeats)
        choice  (->> repeats
                     (into [])
                     doall
                     flatten
                     rand-nth)]
    choice))

