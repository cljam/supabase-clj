(ns co.cljazz.supabase-clj.core
  (:require
   [co.cljazz.supabase-clj.internals.http :refer [get! post!] :as internal.http]
   [co.cljazz.supabase-clj.internals.utils :as utils]))

(defn signin-with-email
  " 
  Disclamer! this function is a opinionated
  Send a user a passwordless link which they can use to redeem an access_token. 
  After they have clicked the link,client will redirect for your config callback link.

  (signin-with-email
   {:base-url ''
    :email ''
    :should-create-user false ; default is true
    :api-key ''
    :options {:email-redirect-to ''}})

  email-redirect-to: is default from your supabase config on their website
  api-key: your api key from supabase
  base-url: your supabase url
  "
  [{:keys [base-url
           email
           api-key
           should-create-user
           options]}]
  (let [params {:body (utils/assoc-some
                       {:email email :create-user true}
                       :create-user should-create-user)
                :options options}
        config {:api-key api-key
                :base-url (str base-url "/auth/v1")}]
    (post! "/otp" params config)))

(defn get-user
  " 
  Get the JSON object for the logged in user.
  token: jwt from your redirect url
  api-key: your api key from supabase
  base-url: your supabase url

 (get-user
   {:token  '' 
    :api-key '' 
    :base-url ''})
  "
  [{:keys [base-url] :as config}]
  (let [base-url (str base-url "/auth/v1")]
    (internal.http/read-json (get! "/user"
                                   (assoc config
                                          :base-url base-url)))))

(defn session-from-url
  "
  If an account is created, users can login to your app.
  This functions returns an user 

  url: your redirect url
  api-key: your api key from supabase
  base-url: your supabase url

  "
  [{:keys [base-url url api-key]}]
  (let [{:keys [refresh-token]} (utils/extract-parameters url)
        base-url (str base-url "/auth/v1")]
    (internal.http/read-json
     (post! "/token?grant_type=refresh_token" {:body {:refresh-token refresh-token}}
            {:api-key api-key
             :base-url base-url}))))

(defn refresh-session
  "
    Generates a new JWT.
    @param refresh-token A valid refresh token that was returned on login.

    api-key: your api key from supabase
    base-url: your supabase url
  "
  [{:keys [base-url
           api-key
           refresh-token]}]
  (let [base-url (str base-url "/auth/v1")]
    (internal.http/read-json
     (post! "/token?grant_type=refresh_token" {:body {:refresh-token refresh-token}}
            {:base-url base-url
             :api-key api-key}))))

