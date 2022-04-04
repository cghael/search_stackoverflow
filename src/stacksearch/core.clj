(ns stacksearch.core
  (:require
    [clojure.pprint :as pp]
    [ring.adapter.jetty :refer [run-jetty]]
    [ring.util.response :refer [redirect response content-type]]
    [ring.middleware.params :refer :all]
    [ring.middleware.defaults :refer :all]
    [compojure.core :refer :all]
    [compojure.route :as route]
    [hiccup.core :refer [html]]
    [stacksearch.api :as api])
  (:gen-class))


(def page
  (str "<html><body>"
       (str "<form>"
            "Search: <input name='search' type='text'>"
            "<input type='submit'>"
            "</form>")
       "</body></html>"))


(defn index-handler
  [_]
  (-> (response page)
      (content-type "text/html")))


(defn search-handler
  [req]
  (let [tags   (-> req :params :tag)
        result (api/search tags)]
    (-> (response result)
        (content-type "application/json"))))


(defroutes app
           (GET "/" request (index-handler request))
           (GET "/search" request (search-handler request))
           (route/not-found "<h1>Page not found</h1>"))


(defn server
  [port host]
  (run-jetty (wrap-defaults app site-defaults)
             {:host  host
              :port  port
              :join? false}))


(defn -main
  "Start point"
  [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "3000"))
        host (or (System/getenv "HOST") "127.0.0.1")]
    (server port host)
    (println (format "> Running server at %s port %d" host port))))
