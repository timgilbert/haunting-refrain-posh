(ns haunting-refrain.fx.datascript
  (:require [re-frame.core :refer [reg-event-fx reg-cofx reg-sub inject-cofx]]
            [shodan.console :as console]
            [posh.core :as posh]
            [datascript.core :as d]))

;; TODO: this might be a good place to put coeffects and whatnot
