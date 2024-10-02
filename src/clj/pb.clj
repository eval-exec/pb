(ns clj.pb
  (:gen-class)
  (:require [ring.adapter.jetty :as jetty]
            [ring.logger :as logger]
            [clojure.pprint]
            [ring.middleware.reload :refer [wrap-reload]]
            [clj.handle :as handle]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (handle/db-init)
  (jetty/run-jetty
   (logger/wrap-with-logger (wrap-reload handle/app))
   {:port 8080 :join? true}))
