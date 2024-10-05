(ns cljs.frontend
  (:require
   [ajax.core :refer [GET POST]]
   [clojure.core.async :refer [chan put!]]
   [reagent.core :as r]
   [reagent.dom.client :as rclient]))

(defn app-header []
  (fn []
    [:h1 [:a {:href "/"} "Pastebin: "]]))
(defn async-get
  [url]
  (let [ch (chan)]
    (GET url {:handler (fn [resp]
                         (put! ch resp))})
    ch))

(defonce root (rclient/create-root (.getElementById js/document "app")))
(defn app-show [uuid content]
  (fn []
    (let [show-url (str "https://" js/window.location.host "/" uuid)
  ;; if content is nil or empty , then async-get content
          content
          (if (or (nil? content) (empty? content))
            (do
              (js/console.log "content is nil or empty")
              (async-get (str "/api/content/" uuid)))
            content)]
      (js/console.log "app-show: content is :" content)
      [:div
       [app-header]
       [:a {:href show-url} show-url]
       [:div
        [:pre
         [:code content]]]])))

(defn app-render-show [uuid]
  (GET (str "/api/content/" uuid)
    {:handler (fn [response]
                (js/console.log "app-render-show: " (type response))
                (let [content (.get response "pastebin/content")
                      show (app-show uuid content)]
                  (rclient/render root [show])))

     :error-handler (fn [response]
                      (js/alert "Failed to fetch content"))}))

(def submitted-atom (r/atom nil))

(defn submit-content-handler [arg]
  (let [url (.get arg "url")]
    ;; (reset! submitted-atom url)
    (app-render-show url)))

;; Function to handle content submission
(defn submit-content [content]
  (POST
    "/api/submit"
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
