(ns gallery-lumin.home-test
  (:require
    [clojure.test :refer :all]
    [ring.mock.request :refer :all]
    [gallery-lumin.handler :refer :all]
    [gallery-lumin.middleware.formats :as formats]
    [muuntaja.core :as m]
    [mount.core :as mount]))

(defn parse-json [body]
  (m/decode formats/instance "application/json" body))

(use-fixtures
  :once
  (fn [f]
    (mount/start
                 #'gallery-lumin.handler/app-routes
                 #'gallery-lumin.routes.home/home-routes)
    (f)))

(deftest test-app
  (testing "main route"
    (let [response ((app) (request :get "/"))]
      (is (= 200 (:status response)))))
  (testing "register route"
    (let [response ((app) (request :post "/register"))]
      (is (= 200 (:status response)))))

    )
