(ns clj.pb
  (:gen-class)
  (:require [ring.adapter.jetty :as jetty]
            [clojure.pprint]
            [ring.middleware.reload :refer [wrap-reload]]
            [eval-exec.handle :as handle]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (handle/db-init)
  (jetty/run-jetty
   (wrap-reload
    handle/app)
   {:port 8080 :join? true}))
