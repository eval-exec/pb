(ns cljs.frontend
  (:require
   [reagent.core :as r]
   ["react" :as react]
   [ajax.core :refer [GET POST]]
   [reagent.dom :as rdom]
   [reagent.dom.client :as rclient]))

;; (defn ^:export run []
;;   (rdom/render [todo-app] (js/document.getElementById "app")))

;; (run)

;; (defn submit-content [content]
;;   (POST "/"
;;     {:params {:content content}
;;      :format :json
;;      :handler #(js/alert "Content submitted successfully")
;;      :error-handler #(js/alert "Failed to submit content")}))

;; (defn load-content [uuid set-content]
;;   (GET (str "/" uuid)
;;     {:handler set-content
;;      :error-handler #(js/alert "Failed to load content")}))

;; (defn content-view [content-atom]
;;   (r/create-class
;;    {:component-did-mount
;;     (fn []
;;       (let [uuid (.-uuid js/window.location.pathname)]
;;         (when uuid
;;           (load-content uuid (fn [data] (reset! content-atom data))))))
;;     :reagent-render
;;     (fn []
;;       [:div [:pre @content-atom]])}))

;; (defn submit-form []
;;   (let [content (r/atom "")]
;;     (fn []
;;       [:div
;;        [:textarea {:placeholder "Paste your content here..."
;;                    :value @content
;;                    :on-change #(reset! content (-> % .-target .-value))}]
;;        [:button {:on-click #(submit-content @content)}
;;         "Submit"]])))

;; (defn app []
;;   (let [content-atom (r/atom nil)]
;;     (fn []
;;       [:div
;;        [:h1 "Pastebin"]
;;        [:div (if @content-atom
;;                [content-view content-atom]
;;                [submit-form])]])))

;; ;; Function to handle content submission
;; (defn submit-content [content]
;;   (POST "/"
;;     {:params {:content content}
;;      :format :json
;;      :handler #(js/alert "Content submitted successfully")
;;      :error-handler #(js/alert "Failed to submit content")}))

;; Component for the submit form
(defn submit-form []
  (let [content (r/atom "")]
    (fn []
      [:div
       [:textarea {:placeholder "Paste your content here..."
                   :value @content
                   :on-change #(reset! content (-> % .-target .-value))}]
       [:button {:on-click #(submit-content @content)}
        "Submit"]])))

;; Main App component
(defn xapp []
  [:div
   [:h1 "Pastebin"]
   [submit-form]])

(defn main []
  ;; (js/console.log "fjeiwohello")
  (rdom/render [xapp]
               (.getElementById js/document "app")))

(main)
