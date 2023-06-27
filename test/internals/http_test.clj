(ns internals.http-test
  (:require
   [gotrue.internals.http :as http]
   [clojure.test :refer [deftest is testing]]))

(deftest req-input->req-map-test
  (testing "it should create request map"
    (is (= {:body "{\"email\":\"test@te.com\"}", 
            :content-type :json, 
            :headers {"apikey" nil}
            :redirectTo "http://t.com/auth/t"}
           (http/input->req-map
            {:body {:email "test@te.com"}
             :options {:emailRedirectTo  "http://t.com/auth/t"}}
            {})))))


(deftest assoc-if-test
  (testing "assoc if should work"
    (is (= {:a 1 :b 2}
           (http/assoc-some {} :a 1 :b 2)))
    (is  (= {:a 1}
            (http/assoc-some {} :a 1 :b nil)))
    (is (= {:a 1 :b false}
           (http/assoc-some {} :a 1 :b false)))
    (is (= {:a 1 :b ""}
           (http/assoc-some {} :a 1 :b "")))))
