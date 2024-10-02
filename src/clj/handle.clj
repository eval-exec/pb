(ns clj.handle
  (:require [jsonista.core :as j]
            [clojure.tools.logging :as logging]
            [compojure.route]
            [compojure.core]
            [next.jdbc :as jdbc]))

(def db {:dbtype "sqlite" :dbname "pastebin.db"})
(def ds (jdbc/get-datasource db))

(defn db-init []
  (jdbc/execute!
   ds
   ["CREATE TABLE IF NOT EXISTS pastebin (uuid TEXT PRIMARY KEY, content TEXT)"]))

(defn all-contents
  [request]
  (let [all-contents
        (jdbc/execute! ds ["SELECT * FROM pastebin"])]
    (str all-contents)))

(defn content-by-id
  [request]
  (let [uuid (:uuid (:params request))
        content  (jdbc/execute-one! ds ["SELECT content FROM pastebin WHERE uuid = ?" uuid])]
    (:pastebin/content content)))

(defn submit-content
  [request]
  ;; Extract the body of the request
  (let [body (slurp (:body request))
        body-value (j/read-value body j/keyword-keys-object-mapper)
        content (:content body-value)
        uuid (java.util.UUID/randomUUID)
        url (format "http://127.0.0.1:3000/%s" uuid)]
    ;; Do something with the body, e.g., save it to a database or process it
    ;; For demonstration purposes, we'll just print the body
    (jdbc/execute! ds ["INSERT INTO pastebin (uuid, content) VALUES (?, ?)" uuid content])
    {:status 200
     :body url}))

(defn page-not-found
  [request]
  "Pastebin: Page Not Found")

(compojure.core/defroutes app
  (compojure.core/GET "/" [] (ring.util.response/file-response "index.html" {:root "public"}))
  (compojure.core/GET "/content/:uuid" params content-by-id) ;; get pastebin content by uuid
  (compojure.core/GET "/all" [] all-contents) ;; get pastebin homepage
  (compojure.core/POST "/" params submit-content) ;; submit pastebin content by post body
  (compojure.route/resources "/")
  (compojure.route/not-found  page-not-found))

;; (compojure.core/defroutes resources-routes
;;   (compojure.route/resources "/"))

;; (app {:uri "/index.html" :request-method :get})
