(ns haunting-refrain.core
  (:require [clojure.string :refer [starts-with?]]
            [ring.middleware.resource :refer [wrap-resource]]))

(defn- wrap-default-index [next-handler]
  (fn [request]
    (next-handler
      (if (or (starts-with? (:uri request) "/css/")  ;; default directory layout after doing
              (starts-with? (:uri request) "/js/"))  ;; `lein new figwheel my-app`
        request
        (assoc request :uri "/index.html")))))  ;; wrap-resource will find index.html for us

(defn- not-found [request]
  {:status 404
   :content/type "text/html"
   :body (format "<p>404: request %s not found!</p>" (:uri request))})

(def handler
  (-> not-found
      (wrap-resource "public")
      wrap-default-index))
