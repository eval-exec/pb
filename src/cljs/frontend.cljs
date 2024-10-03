(ns cljs.frontend
	(:require
	 [reagent.core :as r]
	 ["react" :as react]
	 [ajax.core :refer [GET POST]]
	 [reagent.dom :as rdom]
	 [reagent.dom.client :as rclient]))

(defn submit-content-handler [arg]
	  (let [url (.get arg "url")]
		(js/console.log url)))

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
			  "The atom " [:code "click-count"] " has value: "
			  @submitted-url ". "]
			 [:textarea {:placeholder "Paste your content here..."
									  :cols 120
									  :rows 20
									  :resize "flex"
									  :value @content
									  :on-change #(reset! content (-> % .-target .-value))}]
			 [:div
			  [:button {:on-click #(submit-content @content)}
					   "Submit"]]])))

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
