(ns stacksearch.api
  (:require
    [cheshire.core :refer :all]
    [clj-http.client :as client]
    [com.climate.claypoole :as cp]))


(defn query-params
  [tag]
  {"pagesize" "100"
   "order"    "desc"
   "sort"     "creation"
   "tagged"   tag
   "site"     "stackoverflow"})


(def statistic (atom {}))


(defn get-tag-statistic
  [tag data-vec]
  (let [view-count   (reduce + 0 (map :view_count data-vec))
        answer-count (reduce + 0 (map :answer_count data-vec))]
    (swap! statistic assoc tag {:total    view-count
                                :answered answer-count})))


(defn get-parallel-search
  [tag]
  (let [url        (or (System/getenv "SOURCE_URL") "https://api.stackexchange.com/2.3/search")
        res        (client/get url {:query-params (query-params tag)})
        res-status (:status res)]
    (if (= res-status 200)
      (when-let [res-body (parse-string (:body res) true)]
        (get-tag-statistic (keyword tag) (:items res-body)))
      (println (format "Error! Response status: %s" res-status)))))


(defn search
  [tags]
  (let [max-pool (Integer/parseInt (or (System/getenv "MAX_POOL") "2"))
        _        (reset! statistic {})]
    (doall (cp/pmap max-pool get-parallel-search tags))
    (generate-string @statistic {:pretty true})))
