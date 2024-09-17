(ns pb.core
  (:require [compojure.core :as compojure-core]
            [compojure.route :as compojure-route]
            [clojure.core :as core]
            [clojure.pprint]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.adapter.jetty :as jetty]
            [pb.handle]))

(compojure-core/defroutes app
  (compojure-core/GET "/" [] "<h1>Hello World</h1>") ;; get pastebin homepage
  (compojure-core/GET "/all" [] pb.handle/all-contents) ;; get pastebin homepage
  (compojure-core/GET "/:uuid" params pb.handle/content-by-id) ;; get pastebin content by uuid
  (compojure-core/POST "/" params pb.handle/submit-content) ;; submit pastebin content by post body
  (compojure-route/not-found  pb.handle/page-not-found))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (pb.handle/db-init)
  (println "Hello, World!")
  (jetty/run-jetty (-> app wrap-reload) {:port 3000}))
