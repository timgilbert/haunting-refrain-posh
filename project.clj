(defproject haunting-refrain "0.3.0-SNAPSHOT"
  :pedantic? :warn
  :dependencies [[org.clojure/clojure "1.9.0-alpha14"]
                 [org.clojure/clojurescript "1.9.293"]
                 [reagent "0.6.0"]
                 [re-frame "0.8.0"]
                 [org.clojure/core.async "0.2.395"]
                 [re-com "1.0.0"]
                 [posh "0.5.4"]
                 [datascript "0.15.5"]
                 [hodgepodge "0.1.3"]
                 [cljs-http "0.1.42"]
                 [com.andrewmcveigh/cljs-time "0.4.0"]
                 [shodan "0.4.2"]
                 [functionalbytes/sibiro "0.1.4"]
                 [kibu/pushy "0.3.6"]
                 [com.cemerick/url "0.1.1"]
                 [re-frisk "0.2.2"]
                 [mount "0.1.10"]]

  :plugins [[lein-cljsbuild "1.1.4"]]

  :min-lein-version "2.5.3"

  :source-paths ["src/clj" "src/cljs"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target" "test/js"]

  :figwheel {:css-dirs ["resources/public/css"]
             :ring-handler haunting-refrain.core/handler}

  :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]
                 :host "0.0.0.0"
                 :port 4444}

  :profiles
  {:prod {}
   :dev
   {:source-paths ["src/clj" "etc/repl"]
    :dependencies [[binaryage/devtools "0.8.3"]
                   [ring "1.5.0"]
                   [figwheel-sidecar "0.5.8"]
                   [com.cemerick/piggieback "0.2.1"]]
    :plugins      [[lein-figwheel "0.5.8" :exclusions [org.clojure/clojure]]
                   [lein-shell "0.5.0"]
                   [lein-doo "0.1.7"]
                   [lein-ancient "0.6.10"]]}}

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
                    :external-config      {:devtools/config {:features-to-install :all}
                                           :re-frisk {:enabled true}}
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
                    :optimizations :none}}]}

  :release-tasks [;; Make sure we're up to date
                  ["vcs" "assert-committed"]
                  ["shell" "git" "checkout" "develop"]
                  ["shell" "git" "pull"]
                  ["shell" "git" "checkout" "master"]
                  ["shell" "git" "pull"]

                  ;; Merge develop into master
                  ["shell" "git" "merge" "develop"]

                  ;; Bump version, commit and tag
                  ["change" "version" "leiningen.release/bump-version" "release"]
                  ["vcs" "commit"]
                  ["shell" "git" "tag" "-f" "release-${:version}"]

                  ;; Merge master back into develop
                  ["shell" "git" "checkout" "develop"]
                  ["shell" "git" "merge" "master"]

                  ;; Bump to SNAPSHOT version and commit
                  ["change" "version" "leiningen.release/bump-version" "minor"]
                  ["vcs" "commit"]

                  ;; Checkout to master, build native dependencies and deploy
                  ["shell" "git" "checkout" "master"]

                  ;; Done
                  ["shell" "echo" "Push to GitHub: 'git push origin develop master --tags'"]])
