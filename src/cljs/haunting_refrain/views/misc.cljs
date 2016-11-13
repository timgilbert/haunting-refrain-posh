(ns haunting-refrain.views.misc)

(defn route-not-found [params]
  [:div.container
   [:h1.title "Route Not Found"]
   [:p "Can't find route " [:code "/" (:* params)]]])
