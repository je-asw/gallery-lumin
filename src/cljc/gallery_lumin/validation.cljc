(ns gallery-lumin.validation
  (:require [struct.core :as st]
            [bouncer.core :as b]
            [bouncer.validators :as v]))

(defn registration-errors [{:keys [pass-confirm] :as params}]
  (first
    (b/validate params
                :id v/required
                :pass [v/required
                       [v/min-count 3 :message "password must contain at least 3 characters"]
                       [= pass-confirm :message "re-entred password does not match"]]))
  )
