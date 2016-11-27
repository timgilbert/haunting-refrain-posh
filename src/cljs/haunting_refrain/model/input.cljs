(ns haunting-refrain.model.input
  (:require [clojure.set :as set]
            [clojure.string :as string]
            [shodan.console :as console]))

(def input-weights
  "Weighted probability list. For each field in the list"
  {:foursquare/name
   {:probability 10}
   :foursquare/event
   {:probability 10}
   :foursquare/event-type
   {:probability 5}
   :foursquare/category
   {:probability 3}
   :foursquare/address
   {:probability 3}
   :foursquare/city
   {:probability 2}
   :foursquare/state
   {:probability 1}
   :foursquare/country
   {:probability 1}
   :foursquare/shout
   {:probability 5}
   :foursquare/cross
   {:probability 2}})

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

