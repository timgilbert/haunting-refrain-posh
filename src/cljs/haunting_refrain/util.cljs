(ns haunting-refrain.util
  (:require [cljs-time.coerce :as c]
            [cljs-time.core :as t]
            [cljs-time.format :as f]))

(defn format-time [seconds-since-epoch]
  (->> seconds-since-epoch
       (* 1000)
       (c/from-long)
       (t/to-default-time-zone)
       (f/unparse (f/formatters :rfc822))))
