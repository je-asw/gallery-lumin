(ns gallery-lumin.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[gallery-lumin started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[gallery-lumin has shut down successfully]=-"))
   :middleware identity})
