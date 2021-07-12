(ns gallery-lumin.routes.helper.pdf-helper
  (:require [clj-pdf.core :as pdf]
            [clojure.java.io :as io]
            [gallery-lumin.db.core :as db]
            ))


;;(pdf in out)
(defn create-body [list]
  [:div
   [:div "this is my users in pdf"]
   ]
  [:h3 "pdf title"]
  [:p "i used clj-pdf lib for this"]
  [:table
   [:thead
    [:tr
     [:th "id"]
     [:th "firstname"]
     [:th "lastname"]
     [:th "email"]
     [:th "admin"]
     [:th "is_active"]]
    ]
   [:tbody
    (for [row list]
      ^{:key (:id row)}
      [:tr
       [:td (:id row)]
       ])
    ]
   ]
  )

(def user-template
  (pdf/template [$id $first_name $last_name $email $admin $is_active])
  )

(def user-template-paragraph
  (pdf/template [:paragraph
                 [:heading $id]
                 [:chunk {:style :bold} "occupation: "] $first_name "\n"
                 [:chunk {:style :bold} "place: "] $last_name "\n"
                 [:chunk {:style :bold} "email: "] $email
                 [:chunk {:style :bold} "admin: "] $admin "\n"
                 [:chunk {:style :bold} "isActive: "] $is_active "\n"
                 [:spacer]]))


(defn create-pdf []
  (pdf/pdf [{:header "Users list"}
            (into [:table
                   {:border      false
                    :cell-border false
                    :header      [{:color [0 150 150]} "id" "first_name" "last_name" "email" "admin" "is_active"]}]

                  (user-template (db/get-users))
                  )
            ]


           "doc.pdf")
  ; (create-header)
  ;(create-body)
  ;(create-footer)
  )
