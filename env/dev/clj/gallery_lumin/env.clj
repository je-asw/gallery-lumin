(ns gallery-lumin.env
  (:require
    [selmer.parser :as parser]
    [clojure.tools.logging :as log]
    [gallery-lumin.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[gallery-lumin started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[gallery-lumin has shut down successfully]=-"))
   :middleware wrap-dev})
