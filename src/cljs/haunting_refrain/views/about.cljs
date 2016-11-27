(ns haunting-refrain.views.about)

(defn about-page []
  [:div.container.content
   [:h1.title "About Haunting Refrain"]
   [:p "Haunting Refrain was meticulously crafted out of the finest parentheses by me, "
    [:a {:href "https://timgilbert.wordpress.com/"} "Tim Gilbert"]
    "."]
   [:p "It's all open-source and you can see the source on "
    [:a {:href "https://github.com/timgilbert/haunting-refrain-posh"} "GitHub"]
    "."]
   [:p "Haunting Refrain uses a lot of neat technology, of which the most significant "
    "bits are probably "
    [:a {:href "https://github.com/clojure/clojurescript"} "ClojureScript"] ", "
    [:a {:href "https://github.com/Day8/re-frame"} "re-frame"] ", "
    [:a {:href "https://github.com/tonsky/datascript"} "datascript"] ", and "
    [:a {:href "https://github.com/mpdairy/posh"} "posh"] "."]
   [:p "The front-end styling is via "
    [:a {:href "http://bulma.io/"} "Bulma"] ", as you can probably tell because I'm too "
    "lazy to change the default color pallete."]])
