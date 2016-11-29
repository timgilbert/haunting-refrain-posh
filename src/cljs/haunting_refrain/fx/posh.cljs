(ns haunting-refrain.fx.posh
  (:require [re-frame.core :refer [reg-event-fx reg-fx reg-cofx reg-sub inject-cofx dispatch]]
            [shodan.console :as console]
            [datascript.core :as d]
            [haunting-refrain.datascript.core :as hrd]
            [posh.reagent :as p]))

;; Best current docs for this are here:
;; https://github.com/Day8/re-frame/blob/master/examples/todomvc/src/todomvc/subs.cljs
(defn- posh-pull-subscription
  "hmm"
  [_ [_ pattern eid]]
  (console/log "posh-pull" pattern eid)
  (p/pull (hrd/get-connection) pattern eid))

(reg-sub :posh/pull posh-pull-subscription)

(defn- posh-q-subscription
  "hmm"
  [_ [query & args]]
  (apply p/q query args))

(reg-sub :posh/q posh-q-subscription)
