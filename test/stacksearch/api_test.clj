(ns stacksearch.api-test
  (:require
    [clojure.test :refer :all]
    [clj-http.client :as client]
    [stacksearch.api :as sut]
    [cheshire.core :refer :all]))

(deftest get-parallel-search-test
  (testing "Если получаем ответ со статусом 200"
    (with-redefs [client/get (constantly {:status 200
                                          :body   "{ \"items\": [{ \"view_count\":17, \"answer_count\":0 }] }"})]
      (let [_        (reset! sut/statistic {})
            tag      "test"
            _        (sut/get-parallel-search tag)
            expected {:test {:total 17 :answered 0}}]
        (is (= @sut/statistic expected)))))

  (testing "Если получаем ответ с любым статусом не равным 200"
    (with-redefs [client/get (constantly {:status 500
                                          :body   "{ \"items\": [{ \"view_count\":17, \"answer_count\":0 }] }"})]
      (let [_        (reset! sut/statistic {})
            tag      "test"
            _        (sut/get-parallel-search tag)
            expected {:test {:error "server unavailable"}}]
        (is (= @sut/statistic expected))))))


(deftest search-test
  (with-redefs [client/get      (constantly {:status 200
                                             :body   "{ \"items\": [{ \"view_count\":17, \"answer_count\":0 }] }"})
                generate-string (fn [x _] x)]
    (testing "Если в запросе несколько тегов"
      (let [_        (reset! sut/statistic {})
            tags     ["clojure" "scala"]
            res      (sut/search tags)
            expected {:clojure {:total 17, :answered 0}
                      :scala   {:total 17, :answered 0}}]
        (is (= res expected))))

    (testing "Если в запросе один тег"
      (let [_        (reset! sut/statistic {})
            tags     "clojure"
            res      (sut/search tags)
            expected {:clojure {:total 17, :answered 0}}]
        (is (= res expected))))))
