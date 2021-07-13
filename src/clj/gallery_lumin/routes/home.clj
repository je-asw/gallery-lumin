(ns gallery-lumin.routes.home
  (:require
    [gallery-lumin.layout :as layout]
    [gallery-lumin.db.core :as db]
    [clojure.java.io :as io]
    [gallery-lumin.middleware :as middleware]
    [ring.util.response]
    [ring.util.http-response :as response]
    [gallery-lumin.routes.auth :as auth]
    [gallery-lumin.routes.users :as u]))

(defn home-page [request]
  (layout/render request "home.html"))


(def site-routes
  [
   ["/" {:get {:handler home-page}}]
   ["/login" {:post auth/login!}]
   ["/logout" {:get auth/logout! }]
   ["/register"  {:post auth/register!}]
   ["/users" {:get u/list-users}]
   ["/user/delete" {:delete u/delete-user}]
   ["/export2pdf" {:get u/users-into-pdf}]
   ["/docs" {:get (fn [_]
                    (-> (response/ok (-> "docs/docs.md" io/resource slurp))
                        (response/header "Content-Type" "text/plain; charset=utf-8")))}]])


(defn home-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   (merge site-routes)
  ])

