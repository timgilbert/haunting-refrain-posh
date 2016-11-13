(ns haunting-refrain.datascript.core
  (:require [datascript.core :as d]
            [posh.reagent :as posh]))

(def ^:private hr-schema
  {})

(defn create-connection! []
  (let [conn (d/create-conn hr-schema)]
    (posh/posh! conn)
    conn))
