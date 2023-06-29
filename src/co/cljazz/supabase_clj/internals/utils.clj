(ns co.cljazz.supabase-clj.internals.utils
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
  (when (not= (.indexOf url "#") -1)
    (let [ params-str (subs url (inc (.indexOf url "#")))
          params (str/split params-str #"&")]
      (reduce (fn [result param]
                (let [[raw-key value] (str/split param #"=")
                      key (convert-key raw-key)]
                  (assoc result (keyword key) value)))
              {}
              params))))

