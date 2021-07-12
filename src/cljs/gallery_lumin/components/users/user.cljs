(ns gallery-lumin.components.users.user
  (:require [reagent.session :as session]
            [reagent.core :refer [atom]]
            [ajax.core :refer [GET POST]]

            )
  )


(defn handler-user [response]
  (.log js/console (str response)))

;(defn delete! [row]
 ; (.log js/console (str row))
 ; (Get "/user/delete"
   ;    {:params        @row
  ;      }
  ;     )
 ; )

(defn get-users! []
  (GET "/users"
       {:handler
        #(do (session/put! :users %)
             (handler-user %))
        })
  )



;;set the body of table users-data
(defn users-table-body [response]
  (.log js/console (str response))
  [:tbody
   (for [row response]
     ^{:key (:id row)}
     [:tr [:td (:id row)]
      ;[:td (:pass row)]
      [:td (:last_name row)]
      [:td (:first_name row)]
      [:td (:email row)]
      [:td (:admin row)]
      [:td (:is_active row)]
      [:td (parse-the-timestamp(:last_login row))]
      [:td [:div
            [:button.btn.btn-primary
             ;{:on-click #(delete! row)}
             "delete"]
            ]]
      ])
   ]
  )

;;create table for users-data
(defn user-table []
  [:div
   [:p "Welcome in users page"]]
  [:div
   [:div
    [:table.table.table-striped.table-bordered
     {:cell-spacing "0" :width "100%"}
     [:thead
      [:tr
       [:th "id "] [:th "firstname "]
       [:th "lastname "] [:th "email "]
       [:th "admin "] [:th "Active "]
       [:th "last_login"]
       [:th "Action"]]]

     (when-let [user-list (session/get :users)]
       [users-table-body user-list]
       )
     ]
    ]


   [:div
    [:button.btn.btn-primary
     {:on-click #(get-users!)}
     "Refresh list"]]
   ]
  )



