(ns cljs.frontend
  (:require
   [reagent.core :as r]
   ["react" :as react]
   [ajax.core :refer [GET POST]]
   [reagent.dom :as rdom]
   [reagent.dom.client :as rclient]))

(defn app-header []
  (fn []
    [:h1 [:a {:href "/"} "Pastebin: "]]))

(defonce root (rclient/create-root (.getElementById js/document "app")))

(def content-show (r/atom nil))
(def content-url (r/atom nil))

(defn app-show []
  [:div
   [app-header]
   [:a {:href (str "http://" js/window.location.host "/" @content-url)} (str js/window.location.host "/" @content-url)]
   (js/console.log @content-show)
   [:div
    [:pre
     [:code @content-show]]]])

(defn app-render-show [url]
  (GET (str "/content/" url)
    {:handler (fn [response]
                (js/console.log (type response))
                (reset! content-show (.get response "pastebin/content"))
                (reset! content-url url)
                (rclient/render root [app-show]))
     :error-handler (fn [response]
                      (js/alert "Failed to fetch content"))}))

(def submitted-atom (r/atom nil))

(defn submit-content-handler [arg]
  (let [url (.get arg "url")]
    ;; (reset! submitted-atom url)
    (app-render-show url)))

;; Function to handle content submission
(defn submit-content [content]
  (POST "/"
    {:params {:content content}
     :format :json
     :handler #'submit-content-handler
     :error-handler #(js/alert "Failed to submit content")}))

;; Component for the submit form
(defn submit-form []
  (let [content (r/atom "")]
    (fn []
      [:div
       [:div
        "The atom " [:code "click-count"] " has value:" @submitted-atom " . "]
       [:textarea {:placeholder "Paste your content here..."
                   :style {:width "80%" :height 400}
				   ;; :cols js/window.innerHeight
				   ;; :rows 20
				   ;; :display "flex"
				   ;; :resize "flex"
				   ;; :resize false
                   :value @content
                   :on-change #(reset! content (-> % .-target .-value))}]
       [:div
        [:button {:on-click #(submit-content @content)}
         "Submit"]]])))

;; Main App component
(defn app-submit []
  [:div
   [app-header]
   [submit-form]])

(defn app-render-submit []
  (rclient/render root [app-submit]))

(defn main []
  (js/console.log js/window.location.pathname)
  ;; if pathname is not "/" or "/index.html" then render the show page
  (let [pathname js/window.location.pathname]
    (if-not (or (= pathname "/") (= pathname "/index.html"))
      (app-render-show (subs pathname 1))
      (app-render-submit))))

(main)
