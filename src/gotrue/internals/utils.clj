(ns gotrue.internals.utils 
  (:require
   [clojure.string :as str]))

(defn assoc-some [m & kvs]
  (reduce (fn [acc [k v]]
            (if (some? v)
              (assoc acc k v)
              acc))
          m
          (partition 2 kvs)))


(defn convert-key [key]
  (str/replace key #"_+" "-"))

(defn extract-parameters [url]
  (let [params-str (subs url (inc (.indexOf url "#")))
        params (str/split params-str #"&")]
    (reduce (fn [result param]
              (let [[raw-key value] (str/split param #"=")
                    key (convert-key raw-key)]
                (assoc result (keyword key) value)))
            {}
            params)))

