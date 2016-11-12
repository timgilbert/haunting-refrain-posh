(defproject haunting-refrain "0.1.0-SNAPSHOT"
  :pedantic? :warn
  :dependencies [[org.clojure/clojure "1.9.0-alpha14"]
                 [org.clojure/clojurescript "1.9.293"]
                 [reagent "0.6.0"]
                 [re-frame "0.8.0"]
                 [org.clojure/core.async "0.2.395"]
                 [re-com "1.0.0"]
                 [posh "0.5.4"]
                 [datascript "0.15.4"]
                 [hodgepodge "0.1.3"]
                 [shodan "0.4.2"]
                 [functionalbytes/sibiro "0.1.4"]
                 [kibu/pushy "0.3.6"]
                 [mount "0.1.10"]]

  :plugins [[lein-cljsbuild "1.1.4"]]

  :min-lein-version "2.5.3"

  :source-paths ["src/clj"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"
                                    "test/js"]

  :figwheel {:css-dirs ["resources/public/css"]
             :ring-handler haunting-refrain.core/handler}

  :profiles
  {:dev
   {:dependencies [[binaryage/devtools "0.8.3"]
                   [ring "1.5.0"]]

    :plugins      [[lein-figwheel "0.5.8" :exclusions [org.clojure/clojure]]
                   [lein-doo "0.1.7"]
                   [lein-ancient "0.6.10"]]
    }}

  :cljsbuild
  {:builds
   [{:id           "dev"
     :source-paths ["src/cljs"]
     :figwheel     {:on-jsload "haunting-refrain.core/mount-root"}
     :compiler     {:main                 haunting-refrain.core
                    :output-to            "resources/public/js/compiled/app.js"
                    :output-dir           "resources/public/js/compiled/out"
                    :asset-path           "js/compiled/out"
                    :source-map-timestamp true
                    :preloads             [devtools.preload]
                    :external-config      {:devtools/config {:features-to-install :all}}
                    }}

    {:id           "min"
     :source-paths ["src/cljs"]
     :compiler     {:main            haunting-refrain.core
                    :output-to       "resources/public/js/compiled/app.js"
                    :optimizations   :advanced
                    :closure-defines {goog.DEBUG false}
                    :pretty-print    false}}

    {:id           "test"
     :source-paths ["src/cljs" "test/cljs"]
     :compiler     {:main          haunting-refrain.runner
                    :output-to     "resources/public/js/compiled/test.js"
                    :output-dir    "resources/public/js/compiled/test/out"
                    :optimizations :none}}
    ]}

  )
