(ns clojure-clj-http-examples.03-trust-store
  (:require
   [clj-http.client :as client]
   [schema.core :as s])
  (:import
   [java.security KeyStore]))

(def default-options {:socket-timeout 1000 :connection-timeout 1000})

(s/defn https-get-request
  [url         :- s/Str
   trust-store :- KeyStore]
  (let [options (merge default-options
                       {:trust-store        trust-store
                        :trust-store-type   "jks"
                        :trust-store-pass   ""
                        :insecure?          false})]
    (client/get url options)))
