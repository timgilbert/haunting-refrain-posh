(ns haunting-refrain.fx.http
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [shodan.console :as console]
            [cljs.core.async :refer [<!]]
            [re-frame.core :as re-frame]))

(defn http-effect
  "Persist an object into localStorage at the given key."
  [{:keys [method url on-success on-failure] :as options}]
  (console/log options)
  (go (let [response (<! (http/request
                           {:url url
                            :method method
                            :with-credentials? false}))]
        (if (:success response)
          (re-frame/dispatch [:http/success options (:body response)])
          (re-frame/dispatch [:http/failure options (:body response) (:status response)])))))

(re-frame/reg-fx :http http-effect)

(defn- http-request
  [{:keys [db]} [_ {:keys [endpoint] :as options}]]
  {:db (assoc-in db [:http/requests endpoint] true)
   :http options})

(re-frame/reg-event-fx :http/request http-request)

(defn- http-success
  [{:keys [db]} [_ {:keys [endpoint on-success] :as options} body]]
  (merge
    {:db (update-in db [:http/requests] dissoc endpoint)}
    (when on-success
      {:dispatch [on-success body]})))

(re-frame/reg-event-fx :http/success http-success)

(defn- http-failure
  [{:keys [db]} [_ {:keys [endpoint on-failure] :as options} body status]]
  (merge
    {:db (update-in db [:http/requests] dissoc endpoint)}
    (when on-failure
      {:dispatch [on-failure body status]})))

(re-frame/reg-event-fx :http/failure http-failure)
