(ns gallery-lumin.routes.users
  (:require
    [gallery-lumin.db.core :as db]
    [ring.util.http-response :as response]
    [gallery-lumin.routes.helper.pdf-helper :as pdf-helper]
    [clj-pdf.core :as pdf]
    [clojure.java.io :as io]
    [clj-time.core :as time]))


(defn delete-user [{:keys [body-params]}]
  (println "****params**** " body-params)
  (let [id (:id body-params)]
    (db/delete-user! id)
    (println "****id**** " id)
    (-> {:result :ok}
        (response/ok)
        ))
  )


(defn list-users [req]
  (response/ok (db/get-users)))


(def now (time/now))


(def user-list
  [{:id "test", :first_name "first name", :last_name "lastname test", :email "test@hotmail.com", :admin "0", :is_active "0"}])

(defn get-pdf [out]
  (try
    (pdf/pdf [{:header "List users"}
              (into [:table
                     {:border      false
                      :cell-border false
                      :header      ["id" "first_name" "last_name" "email" "admin" "is_active"]}]
                    (for [user user-list]
                      [[:cell (:id user)]
                       [:cell (:first_name user)]
                       [:cell (:last_name user)]
                       [:cell (:email user)]
                       [:cell (:admin user)]
                       [:cell (:is_active user)]])
                    )
              ]
             "doc.pdf")
    (catch Exception e
      (e))
    )
  )

(defn write-response [file-bytes]
  (with-open [in (java.io.ByteArrayInputStream. file-bytes)]
    (-> (response/resource-response in)
        (response/header "Content-Disposition" "filename= userlist.pdf")
        (response/header "Content-Length" (count file-bytes))
        (response/content-type "application/pdf"))
    )
  )

;;insert data into pdf
(defn users-into-pdf [req]
  (try
    (let [out (java.io.ByteArrayOutputStream.)]
      (get-pdf out)
      (write-response (.toByteArray out))
      )
    (catch Exception e
      (println e)
      ))
  )
