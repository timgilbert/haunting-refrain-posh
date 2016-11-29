(ns haunting-refrain.fx.datascript
  (:require [re-frame.core :refer [reg-event-fx reg-fx reg-cofx reg-sub inject-cofx dispatch]]
            [shodan.console :as console]
            [datascript.core :as d]
            [haunting-refrain.datascript.core :as hrd]))

(defn- datascript-conn-coeffect
  "Gets a datascript connection and saves it in the :ds/conn coeffect."
  [coeffects key]
  (assoc coeffects :ds/conn (hrd/get-connection)))
(reg-cofx :ds/conn datascript-conn-coeffect)

(defn- datascript-db-coeffect
  "Gets a datascript database value from the active connection and saves it in the :ds/db coeffect."
  [coeffects key]
  (assoc coeffects :ds/conn (d/db (hrd/get-connection))))
(reg-cofx :ds/db datascript-db-coeffect)

(defn- datascript-transact!-effect
  "Effect which takes a bunch of transaction data to transact and optional on-success and on-failure
  event vectors. The effect will transact the given data into the active connection. If on-success is
  present, the result of the (d/transact!) call will be appended to it."
  [tx-data on-success on-failure]
  {:pre [(or (nil? on-success) (vector? on-success))
         (or (nil? on-failure) (vector? on-failure))]}
  (try
    (let [result (d/transact! (hrd/get-connection) tx-data)]
      (when on-success
        (dispatch (conj on-success result))))
    (catch :default e
      (console/error "Exception transacting" tx-data ":" e)
      (when on-failure
        (dispatch (conj on-failure e))))))
(reg-fx :ds/transact! datascript-transact!-effect)
