(ns gallery-lumin.routes.users
  (:require
    [gallery-lumin.db.core :as db]
    [ring.util.http-response :as response]
    [gallery-lumin.routes.helper.pdf-helper :as pdf-helper]
    [clj-pdf.core :as pdf]))


(def user-template
  (pdf/template [$id $first_name $last_name $email $admin $is_active])
  )

(defn list-users [req]

  (response/ok (db/get-users)))

(defn users-into-pdf []
  (try (pdf/pdf [{:header "Users list"}
                 (into [:table
                        {:border      false
                         :cell-border false
                         :header      [{:color [0 150 150]} "id" "first_name" "last_name" "email" "admin" "is_active"]}]

                       (user-template (db/get-users))
                       )
                 ]
                "doc.pdf")
       (-> {:result :ok}
           (response/ok)
           ; (assoc :session (assoc session :identity (:id)))
           )
       (catch Exception e
         (e)
         ))
  )
