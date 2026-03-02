(ns clojure-clj-http-examples.04-mtls
  (:require
   [clj-http.client :as client]
   [schema.core :as s])
  (:import
   [java.security KeyStore]))

(s/defn https-get-request
  [url         :- s/Str
   key-store   :- KeyStore
   trust-store :- KeyStore]
  (client/get url {:socket-timeout 1000
                   :connection-timeout 1000
                   :keystore           key-store
                   :keystore-pass      "changeit"
                   :keystore-type      "PKCS12"
                   :trust-store        trust-store
                   :trust-store-type   "jks"
                   :trust-store-pass   ""
                   :insecure?          false}))

