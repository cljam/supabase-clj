(ns gotrue.internals.http
  (:require
   [clj-http.client :as client]
   [clojure.data.json :as json]
   [clojure.pprint :as pprint]
   [clojure.string :as str]))

(add-tap (bound-fn* clojure.pprint/pprint))

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


;(assoc-some {} :a 1 :b 2 :c nil :d false :e "") ;=> {:a 1 :b 2}
(defn assoc-some [m & kvs]
  (reduce (fn [acc [k v]]
            (if (some? v)
              (assoc acc k v)
              acc))
          m
          (partition 2 kvs)))


(defn ^:private with-options
  [req-map {:keys [options]}]
  (if options
    (assoc-some req-map
           :redirectTo (:emailRedirectTo options)) 
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

(defn ^:private as-api-response [http-response options]
  (let [body    (:body http-response)]
    body))

(defn input->req-map [params config]
  (-> {}
      (with-headers config)
      (with-body params)
      (with-options params)
      ))

(defn post! [path params config]
  (let [url      (config+path->url config path)
        req-map (input->req-map params config)
        response (client/post url req-map)]
    (as-api-response response {})))

(defn get!
  [path config & {:as options}]
  (let [url      (config+path->url config path)
        req-map  (-> {}
                     (with-headers config))
        response (client/get url req-map)
        options  (merge {:parse? true} options)]
    (as-api-response response options)))
