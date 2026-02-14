(ns clojure-clj-http-examples.01-timeout
  (:require
   [clj-http.client :as client]
   [schema.core :as s]))

(s/defn http-get-request
  [url :- s/Str]
  (client/get url {:socket-timeout 1000 :connection-timeout 1000}))
