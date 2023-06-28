(ns gotrue.core
  (:require
   [gotrue.internals.http :refer [get! post!] :as internal.http]
   [gotrue.internals.utils :as utils]))

(defn signin-with-email
  [{:keys [base-url
           email
           api-key
           should-create-user
           options]}]
  (let [params {:body (utils/assoc-some {:email email :create-user true}
                                        :create-user should-create-user)
                :options options}
        config {:api-key api-key
                :base-url (str base-url "/auth/v1")}]
    (post! "/otp" params config)))

(defn get-user
  [{:keys [base-url] :as config}]
  (let [base-url (str base-url "/auth/v1")]
    (internal.http/read-json (get! "/user"
                                   (assoc config
                                          :base-url base-url)))))

(defn session-from-url [url])

(defn session
  [token api-key base-url]
  (let [url (str base-url "/auth/v1/reauthenticate")]
    (internal.http/read-json (get! url {:api-key api-key :token token}))))

(comment

 
  )


