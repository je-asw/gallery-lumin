(ns gallery-lumin.routes.users
  (:require
    [gallery-lumin.db.core :as db]
    [ring.util.http-response :as response]))


(defn list-users [req]

  (response/ok (db/get-users)))
