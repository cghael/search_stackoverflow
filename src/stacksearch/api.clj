(ns stacksearch.api
  (:require
    [cheshire.core :refer :all]
    [clj-http.client :as client]
    [com.climate.claypoole :as cp]
    [clojure.pprint :as pp]))


(defn query-params
  [tag]
  {"pagesize" "1"
   "order"    "desc"
   "sort"     "creation"
   "tagged"   tag
   "site"     "stackoverflow"})


(def statistic (atom {}))


(defn get-tag-statistic
  "Formats and adds data, received by one request into atom"
  [tag data-vec]
  (let [view-count   (reduce + 0 (map :view_count data-vec))
        answer-count (reduce + 0 (map :answer_count data-vec))]
    (swap! statistic assoc tag {:total    view-count
                                :answered answer-count})))


(defn get-parallel-search
  "Sends request to the server, processes the result"
  [tag]
  (try
    (let [url        (or (System/getenv "SOURCE_URL") "https://api.stackexchange.com/2.3/search")
          res        (client/get url {:query-params (query-params tag)})
          res-status (:status res)]
      (if (= res-status 200)
        (let [res-body (parse-string (:body res) true)]
          (get-tag-statistic (keyword tag) (:items res-body)))
        (swap! statistic assoc (keyword tag) {:error "Server unavailable"})))
    (catch Exception e
      (swap! statistic assoc (keyword tag) {:error "HostException. Server unavailable"}))))


(defn search
  [tags]
  (let [max-pool (Integer/parseInt (or (System/getenv "MAX_POOL") "2"))
        _        (reset! statistic {})
        tags'    (if (vector? tags) tags (vector tags))]
    (doall (cp/pmap max-pool get-parallel-search tags'))
    (generate-string @statistic {:pretty true})))