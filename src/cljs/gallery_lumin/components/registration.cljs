(ns gallery-lumin.components.registration
  (:require [reagent.session :as session]
            [reagent.core :refer [atom]]
            [ajax.core :refer [POST]]
            [gallery-lumin.validation :refer [registration-errors]]
            [gallery-lumin.components.common :as c]))

(enable-console-print!)

(defn register! [fields errors]
  (reset! errors (registration-errors @fields))
  (println fields)
  (when-not @errors
    (POST
      "/register"
      {:params        @fields
       :handler       #(do (session/put! :identity (:id @fields))
                           (reset! fields {})
                           ;(session/remove! :modal)
                           )
       :error-handler #(reset!
                         errors
                         {:server-error (get-in % [:response :message])})}))
  )

(defn registration-form []
  (let [fields (atom {}) error (atom nil)]
    (fn []
      (println fields)
       ;modal header
       [:div [:h4 "Registration"]]
       ;modal body
       [:div
        [:div.well.well-sm
         [:strong "* required field"]]
        [c/text-input "name" :id "enter a user name" fields]
        (when-let [error (first (:id @error))]
          [:div.alert.alert-danger error])
        [c/password-input "password" :pass "enter a password" fields]
        (when-let [error (first (:pass @error))]
          [:div.alert.alert-danger error])
        [c/password-input "confirm-password" :pass-confirm "re-enter the password" fields]
        (when-let [error (:server-error @error)]
          [:div.alert.alert-danger error])

        [c/text-input "firstname" :first_name "enter a firstname" fields]
        [c/text-input "lastname" :last_name "enter a lastname" fields]
        [c/text-input "email" :email "your_email@mail.com" fields]
        ;modal footer
       [:div
        [:button.btn.btn-primary
         {:on-click #(register! fields error)}
         "Register"]
        [:button.btn.btn-danger
         {:on-click #(session/remove! :modal)}
         "Cancel"]]])))

(defn registration-button []
  [:a.btn
   {:on-click #(session/put! :modal registration-form)}
   "register"])
