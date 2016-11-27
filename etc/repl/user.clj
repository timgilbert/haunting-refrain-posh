(ns user)

(println "Loading sidecar...")
(require '[figwheel-sidecar.repl-api :as ra])

(defn fig []
  (ra/start-figwheel!))
(defn stop-fig []
  (ra/stop-figwheel!))

(defn cljs []
  (ra/cljs-repl "dev"))
