(ns clj.pb
  (:gen-class)
  (:require [ring.adapter.jetty :as jetty]
            [ring.logger :as logger]
            [clojure.pprint]
            [ring.middleware.defaults]
            [ring.middleware.json]
            [ring.middleware.reload :refer [wrap-reload]]
            [clj.handle :as handle]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (handle/db-init)
  (jetty/run-jetty
   (logger/wrap-with-logger
    ;; (ring.middleware.defaults/wrap-defaults
    (wrap-reload
     (ring.middleware.json/wrap-json-response
      handle/app))
      ;; ring.middleware.defaults/site-defaults
	  ;; )
    )
   {:port 8080 :join? true}))
