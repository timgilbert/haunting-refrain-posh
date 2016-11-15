(ns haunting-refrain.util
  (:require [cljs-time.coerce :as c]
            [cljs-time.format :as f]))

(defn format-time [time-in-long]
  (f/unparse (f/formatters :rfc822) (c/from-long time-in-long)))
