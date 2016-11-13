(ns haunting-refrain.fx.core
  (:require haunting-refrain.fx.auth
            haunting-refrain.fx.navigation
            [re-frame.core :as re-frame]
            [cemerick.url :as url]))

(def default-db
  {:route/current-page :main/index})

(re-frame/reg-event-db
  :initialize-db
  (fn  [_ _]
    default-db))

(defn url-fragment-coeffect
  "The coeffect will examine the current page's URL and treat the "
  [coeffects _]
  (let [frag-map (-> (.. js/window -location -href)
                     (url/url)
                     :anchor
                     (url/query->map))]
    (assoc coeffects :url-fragment frag-map)))

(re-frame/reg-cofx :url-fragment url-fragment-coeffect)
