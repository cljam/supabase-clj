(ns gotrue.core
  (:require
   [clj-http.client :as client]
   [clojure.data.json :as json]
   [clojure.pprint :as pprint]
   [clojure.string :as str]))

(defn ^:private json->clj-keys
  [orig-key]
  (-> orig-key
      (str/replace #"_" "-")
      keyword))

(defn read [payload]
  (json/read-str payload {:key-fn json->clj-keys}))

(defn ^:private clj->json-keys
  [orig-key]
  (-> orig-key
      name
      (str/replace #"-" "_")))

(defn write-json [payload]
  (json/write-str payload {:key-fn clj->json-keys}))

(defn ^:private with-body
  [req-map {:keys [body]}]
  (if body
    (merge req-map
           {:body         (write-json body)
            :content-type :json})
    req-map))

(defn config+path->url [config path]
  (str (:base-url config) path))

(defn ^:private config->headers
  [{:keys [api-key token]}]
  (if  token
    {"apikey" api-key
     "Authorization" (str "Bearer " token)}
    {"apikey" api-key}))

(defn ^:private with-headers
  [req-map config]
  (merge req-map {:headers (config->headers config)}))

(defn ^:private as-api-response [http-response & {:as options}]
  (let [body    (:body http-response)
        options (merge {:parse? true} options)]
    body))

(defn post! [path params config]
  (let [url      (config+path->url config path)
        req-map  (-> {}
                     (with-headers config)
                     (with-body params))
        response (client/post url req-map)]
    (as-api-response response)))

(defn get!
  [path config & {:as options}]
  (let [url      (config+path->url config path)
        req-map  (-> {}
                     (with-headers config))
        response (client/get url req-map)
        options  (merge {:parse? true} options)]
    (as-api-response response options)))

(defn request-signin-email
  [base-url email api-key]
  (let [url (str base-url "/auth/v1/otp")]
    (post! url {:body {:email email}} {:api-key api-key})))

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

  

  (request-signin-email ""
                        ""
                        ""))

