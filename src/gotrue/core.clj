(ns gotrue.core
  (:require
   [internals.http :refer [post! get!]]))

(defn request-signin-email
  [base-url email api-key]
  (let [url (str base-url "/auth/v1/otp")]
    (post! url {:body {:email email} :redirectTo "http://localhost:3000/api/auth/login"}
           {:api-key api-key})))

(defn get-user
  [token api-key base-url]
  (let [url (str base-url "/auth/v1/user")]
    (read (get! url {:api-key api-key :token token}))))

(defn session
  [token api-key base-url]
  (let [url (str base-url "/auth/v1/reauthenticate")]
    (read (get! url {:api-key api-key :token token}))))

(comment

  (get-user
   "jwt"
   "api"
   "url")

  )


