(ns internals.utils-test
  (:require
   [clojure.test :refer [deftest is testing]]
   [gotrue.internals.utils :as utils]))

(deftest assoc-if-test
  (testing "assoc if should work"
    (is (= {:a 1 :b 2}
           (utils/assoc-some {} :a 1 :b 2)))
    (is  (= {:a 1}
            (utils/assoc-some {} :a 1 :b nil)))
    (is (= {:a 1 :b false}
           (utils/assoc-some {} :a 1 :b false)))
    (is (= {:a 1 :b ""}
           (utils/assoc-some {} :a 1 :b "")))))

(deftest get-params-from-url-test
  (testing "get params from url should work"
    (is (= {:refresh-token "kgyByE7FSwxW5r2tUjzS0w"
            :token-type "bearer"
            :expires-in "3600"
            :type "magic-link" 
            :access-token "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhRoZW50aWNhdGVkIiwiZXhwIj4ZWU4ZjI2LTRlNjctNDA0GNhOTAxNyb2xlIjoiYXV0aGVudGljYXRlZCIsImFhbCI6ImFhbDEiLCJhbXIiOlt7Im1ldGhvZCI6Im90cCIsInRpbWVzdGFtcCI6MTY4Nzk1M.E47xqL4Z8y9bj91FqOgZ5zzhNz4cY4KWharn1QfYTPQ"}
           (utils/extract-parameters
            "http://localhost:3000/api/auth/login#access_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhRoZW50aWNhdGVkIiwiZXhwIj4ZWU4ZjI2LTRlNjctNDA0GNhOTAxNyb2xlIjoiYXV0aGVudGljYXRlZCIsImFhbCI6ImFhbDEiLCJhbXIiOlt7Im1ldGhvZCI6Im90cCIsInRpbWVzdGFtcCI6MTY4Nzk1M.E47xqL4Z8y9bj91FqOgZ5zzhNz4cY4KWharn1QfYTPQ&expires_in=3600&refresh_token=kgyByE7FSwxW5r2tUjzS0w&token_type=bearer&type=magiclink")))
    (is (= {:a "1" :b "2"}
           (utils/extract-parameters
            "http://t.com?a=1&b=2")))
    (is (= {}
           (utils/extract-parameters "http://t.com")))
    (is (= {}
           (utils/extract-parameters "http://t.com#t")))))
