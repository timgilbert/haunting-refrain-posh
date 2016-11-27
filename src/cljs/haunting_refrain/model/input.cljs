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

(def state-abbreviations
  "Whee!"
  {"AL" "Alabama"
   "AK" "Alaska"
   "AS" "American Samoa"
   "AZ" "Arizona"
   "AR" "Arkansas"
   "CA" "California"
   "CO" "Colorado"
   "CT" "Connecticut"
   "DE" "Delaware"
   "DC" "District Of Columbia"
   "FM" "Federated States Of Micronesia"
   "FL" "Florida"
   "GA" "Georgia"
   "GU" "Guam"
   "HI" "Hawaii"
   "ID" "Idaho"
   "IL" "Illinois"
   "IN" "Indiana"
   "IA" "Iowa"
   "KS" "Kansas"
   "KY" "Kentucky"
   "LA" "Louisiana"
   "ME" "Maine"
   "MH" "Marshall Islands"
   "MD" "Maryland"
   "MA" "Massachusetts"
   "MI" "Michigan"
   "MN" "Minnesota"
   "MS" "Mississippi"
   "MO" "Missouri"
   "MT" "Montana"
   "NE" "Nebraska"
   "NV" "Nevada"
   "NH" "New Hampshire"
   "NJ" "New Jersey"
   "NM" "New Mexico"
   "NY" "New York"
   "NC" "North Carolina"
   "ND" "North Dakota"
   "MP" "Northern Mariana Islands"
   "OH" "Ohio"
   "OK" "Oklahoma"
   "OR" "Oregon"
   "PW" "Palau"
   "PA" "Pennsylvania"
   "PR" "Puerto Rico"
   "RI" "Rhode Island"
   "SC" "South Carolina"
   "SD" "South Dakota"
   "TN" "Tennessee"
   "TX" "Texas"
   "UT" "Utah"
   "VT" "Vermont"
   "VI" "Virgin Islands"
   "VA" "Virginia"
   "WA" "Washington"
   "WV" "West Virginia"
   "WI" "Wisconsin"
   "WY" "Wyoming"
   ;; I would feel bad if I left these out, and they'd be too polite to mention it
   "AB" "Alberta"
   "BC" "British Columbia"
   "MB" "Manitoba"
   "NB" "New Brunswick"
   "NL" "Newfoundland and Labrador"
   "NS" "Nova Scotia"
   "NU" "Nunavut"
   "NT" "Northwest Territories"
   "ON" "Ontario"
   "PE" "Prince Edward Island"
   "QC" "Quebec"
   "SK" "Saskatchewan"
   "YT" "Yukon"})

(defn- dispatch-field [field _]
  (case field :foursquare/address :address
              :foursquare/state   :state
              :default))
(defmulti sanitize-datum dispatch-field)

(defmethod sanitize-datum :address [_ datum]
  ;; Remove initial numeric bit of address to get street name
  (string/replace-first datum #"^[0-9]+[0-9a-zA-Z]+\s+" ""))

(defmethod sanitize-datum :state [_ datum]
  ;; Remove initial numeric bit of address to get street name
  (get state-abbreviations datum datum))

(defmethod sanitize-datum :default [_ datum]
  datum)

(defn random-field
  "Inartful, inefficient implementation of weighted probability list selection. Looking
  at input-field, select one of its keys based on the input-weight field above."
  [input-data]
  (let [domain  (set/intersection (-> input-data keys set) (-> input-weights keys set))
        repeats (for [field domain
                      :let [p (get-in input-weights [field :probability])]]
                  (repeat p field))
        choice  (->> repeats
                     (into [])
                     doall
                     flatten
                     rand-nth)]
    choice))

