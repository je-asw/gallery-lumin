(ns gallery-lumin.components.login
  (:require [reagent.session :as session]
            [reagent.core :refer [atom]]
            [goog.crypt.base64 :as b64]
            [clojure.string :as string]
            [ajax.core :refer [POST GET]]
            [gallery-lumin.components.common :as c])
  )
(enable-console-print!)


;;set time in session
(def timeout-ms (* 1000 60 30))

(defn session-timer []
  (when (session/get :identity)
    (if (session/get :user-event)
      (do(session/remove! :user-event)
         (js/setTimeout #(session-timer) timeout-ms))
      (session/remove! :identity)))
  )

;generate the authorization header
(defn encode-auth [user pass]

  (println "encode" user pass)
  (->>(str user ":" pass)
      (b64/encodeString)
      (str "Basic ")))

;login-function
(defn login! [fields error]
  (println fields)
  (let [{:keys [id pass]} @fields]
    (reset! error nil)
    (POST "/login"
               {:headers {"Authorization"
                          (encode-auth (string/trim id) pass)}
                :handler #(do
                           ; (session/remove! :modal)
                            (session/put! :identity id)
                            (js/setTimeout session-timer timeout-ms)
                            (reset! fields nil))
                :error-handler #(reset! error (get-in % [:response :message]))
                }))
  )

(defn login-form []
  (let [fields (atom {}) error (atom nil)]
    (fn []
      [:div
       [:div "User Login"]
       [:div
        [:div.well.well-sm
         [:strong "* required field"]]
        [c/text-input "name" :id "enter a user name" fields]
        (when-let [error (first (:id @error))]
          [:div.alert.alert-danger error])
        [c/password-input "password" :pass "enter a password" fields]
        (when-let [error (first (:pass @error))]
          [:div.alert.alert-danger error])]

       [:div
        [:button.btn.btn-primary
         {:on-click #(login! fields error)}
         "Login"]
        [:button.btn.btn-danger
         {:on-click #(session/remove! :modal)}
         "Cancel"]]
       ]
       )))

(defn login-button []
  [:a.btn
   {:on-click #(session/put! :modal login-form)}
   "login"])
