(ns gallery-lumin.routes.auth
  (:require
    [ring.util.http-response :as response]
    [buddy.hashers :as hashers]
    [clojure.tools.logging :as log]
    [gallery-lumin.db.core :as db]
    [gallery-lumin.validation :refer [registration-errors] ]))

(defn decode-auth [encoded]
  (println "decode-auth" encoded)
  (let [auth (second (.split encoded " "))]
    (-> (.decode (java.util.Base64/getDecoder) auth)
        (String. (java.nio.charset.Charset/forName "UTF-8"))
        (.split":")))
  )

(defn authenticate [[id pass]]
  (println "********authentication*********" id pass)
  (when-let [user (first (db/get-user {:id id}))]
    (when (hashers/check pass (:pass user))
      id)))

(defn login! [{:keys [params]}]
  (println "*****login!********" params)
  (if-let[id (authenticate (decode-auth params))]
    (-> {:result :ok}
        (response/ok)
        ;(assoc :session (assoc session :identity id))
        )
    (response/unauthorized {:result :unauthorized
                            :message "login failure"}))
  )
;; ------------------------------------
;;logout function reset the session
(defn logout! []
  (-> {:result :ok}
      (response/ok)
      (assoc :session nil))
  )

;;--------------------------------------
;;this function handle us error
(defn handle-registration-error [e]
  (if (-> e

          (.getMessage)
          (.startsWith "ERROR: duplicate key value"))
    (response/precondition-failed
      {:result  :error
       :message "user with the selected ID already exists"})
    (do (log/error e)
        (response/internal-server-error
          {:result  :error
           :message "server error occurred while adding the user"}))
    )
  )


(defn register! [{:keys [params]}]
  (println "register:" params)

  ;(if (registration-errors user)
   ; (response/precondition-failed {:result :error})
    (try
      (db/create-user!
        (-> params
           ; :params (select-keys [:id :pass :pass-confirm])
            (assoc :is_active 1 :admin 0)
            (dissoc :pass-confirm)
            (update :pass hashers/encrypt)
            )
        )
      (-> {:result :ok}
          (response/ok)
         ; (assoc :session (assoc session :identity (:id)))
          )
      (catch Exception e
        (handle-registration-error e)
        ))
   ; )
  )
