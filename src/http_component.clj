(ns http-component
  (:require [com.stuartsierra.component :as component]
            [clj-http.util :as http-util]
            [clj-http.client :as http]))

(defn request-fn
  "Accepts :req which should be a map containing the following keys:
  :url - string, containing the http address requested
  :method - keyword, contatining one of the following options:
    #{:get :head :post :put :delete :options :copy :move :patch}

  The following keys make an async HTTP request, like ring's CPS handler.
  * :respond
  * :raise"
  [{:keys [url] :as req} & [respond raise]]
  (http/check-url! url)
  (if (http-util/opt req :async)
    (if (some nil? [respond raise])
      (throw (IllegalArgumentException.
              "If :async? is true, you must pass respond and raise"))
      (http/request (dissoc req :respond :raise) respond raise))
    (http/request req)))

(defprotocol HttpProvider
  (request [self request-input]))

(defrecord Http [_]

  HttpProvider
  (request
    [_self {:keys [method url] :as request-input}]
    (let [start-time (System/currentTimeMillis)
          {:keys [status] :as response} (request-fn request-input)
          end-time (System/currentTimeMillis)
          total-time (- end-time start-time)]
      response)))

(defn new-http [] (map->Http {}))
