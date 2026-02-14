(ns clojure-clj-http-examples.02-json
  (:require
   [camel-snake-kebab.core :as camel-snake-kebab]
   [cheshire.core :as cheshire]
   [clj-http.client :as client]
   [schema.core :as s]))

(s/set-fn-validation! true)

(s/defschema User
  "A user"
  {:id s/Num
   :name s/Str
   :email s/Str
   :username s/Str
   :phone s/Str
   :website s/Str
   :company {s/Keyword s/Any}
   :address {s/Keyword s/Any}})

(s/defschema Post
  "A post"
  {(s/optional-key :id) s/Int
   :user-id s/Int
   :title s/Str
   :description s/Str})

(s/defn get-user :- User
  [user-id :- s/Num]
  (->> (client/get (str "https://jsonplaceholder.typicode.com/users/" user-id)
                   {:socket-timeout 1000
                    :connection-timeout 1000
                    :as :json}) ;; cheshire
       :body
       (s/validate User)))

(s/defn save-post :- Post
  [post :- Post]
  (s/validate Post
              (-> (client/post "https://jsonplaceholder.typicode.com/posts"
                               {:content-type "application/json"
                                :accept "application/json"
                                :socket-timeout 1000
                                :connection-timeout 1000
                                :body (cheshire/generate-string post {:key-fn camel-snake-kebab/->camelCaseString})})
                  :body
                  (cheshire/decode camel-snake-kebab/->kebab-case-keyword))))

(s/defn update-post :- Post
  [post :- Post]
  (s/validate Post
              (-> (client/put (str "https://jsonplaceholder.typicode.com/posts/" (:id post))
                              {:content-type "application/json"
                               :accept "application/json"
                               :socket-timeout 1000
                               :connection-timeout 1000
                               :body (cheshire/generate-string post {:key-fn camel-snake-kebab/->camelCaseString})})
                  :body
                  (cheshire/decode camel-snake-kebab/->kebab-case-keyword))))

(s/defn delete-user
  [user-id :- s/Int]
  (client/delete (str "https://jsonplaceholder.typicode.com/users/" user-id)
                 {:socket-timeout 1000
                  :connection-timeout 1000}))

(comment
  (get-user 1)

  (save-post {:user-id 1
              :title "My first post"
              :description "This is my first post"})

  (update-post {:id 1
                :user-id 1
                :title "My first post"
                :description "This is my first post"})

  (delete-user 1))
