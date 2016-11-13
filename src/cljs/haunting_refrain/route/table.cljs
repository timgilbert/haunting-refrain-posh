(ns haunting-refrain.route.table
  (:require [mount.core :refer [defstate]]
            [sibiro.core :as sibiro]
            [shodan.console :as console]
            [haunting-refrain.views.foursquare :as foursquare]
            [haunting-refrain.views.misc :as misc]
            [haunting-refrain.views.index :as index]))


(def ^:private route-definitions
  "Big route table. Each route is a [method route-pattern handler tag-keyword] tuple."
  {:main/index
   {:route/url  "/"
    :route/page index/index-page}
   :main/about
   {:route/url  "/about"
    :route/page index/about-page}
   :foursquare/login
   {:route/url  "/foursquare"
    :route/page foursquare/foursquare-page}
   :foursquare/hello
   {:route/url  "/foursquare-hello"
    :route/page foursquare/hello-page}
   :main/route-not-found
   {:route/url  "/:*"
    :route/page misc/route-not-found}})

(defn page-for
  "Given a route keyword from the above map, return its :route/page value"
  [route-keyword]
  (if-let [page (get-in route-definitions [route-keyword :route/page])]
    page
    (do
      (console/error "Unknown route keyword " route-keyword " requested!")
      misc/route-not-found)))

(defn to-sibiro-table
  "Convert the above route definitions to a sibiro input set, looking like this:
  #{[:get url handler] [:get url handler] ...}"
  [definitions]
  (into #{}
        (for [[route-key {:keys [:route/url]}] definitions]
          [:get url route-key])))

(defn compile-all-routes []
  (-> route-definitions to-sibiro-table sibiro/compile-routes))

(defstate all-routes :start (compile-all-routes))
