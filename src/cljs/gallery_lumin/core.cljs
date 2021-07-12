(ns gallery-lumin.core
  (:require
    [day8.re-frame.http-fx]
    [reagent.dom :as rdom]
    [reagent.core :as r]
    [re-frame.core :as rf]
    [goog.events :as events]
    [goog.history.EventType :as HistoryEventType]
    [markdown.core :refer [md->html]]
    [gallery-lumin.ajax :as ajx]
    [gallery-lumin.events]
    [reitit.core :as reitit]
    [reitit.frontend.easy :as rfe]
    [clojure.string :as string]
    [gallery-lumin.components.registration :as reg]
    [gallery-lumin.components.login :as l]
    [gallery-lumin.components.common :as c]
    [ajax.core :as ajax]
    [reagent.session :as session]
    [gallery-lumin.components.users.user :as u]
    )
  (:import goog.History))

(defn nav-link [uri title page]
  [:a.navbar-item
   {:href   uri
    :class (when (= page @(rf/subscribe [:common/page-id])) :is-active)}
   title])
;;-----------------------
;;user-menu component
(defn user-menu []
  (if-let [id (session/get :identity)]
    [:ul.nav.navbar-nav.pul-l-right
     ;[:li.nav-item [u/upload-button]]
     [:li.nav-item
      [:a.dropdown-item.btn
       {:on-click #(ajax/POST "/logout"
                              {:handler (fn [] (session/remove! :identity))})}
       [:i.fa.fa-user] " " id " | sign out"]]]
    [:ul.nav.navbar-nav.pul-l-right
     [:li.nav-item [l/login-button]]
     [:li.nav-item [reg/registration-button]]
     ]))

;;-----------------------------
;;navbar component
(defn navbar [] 
  (r/with-let [expanded? (r/atom false)]
              [:nav.navbar.is-info>div.container
               [:div.navbar-brand
                [:a.navbar-item {:href "/" :style {:font-weight :bold}} "gallery-lumin"]
                [:span.navbar-burger.burger
                 {:data-target :nav-menu
                  :on-click #(swap! expanded? not)
                  :class (when @expanded? :is-active)}
                 [:span][:span][:span]]]
               [:div#nav-menu.navbar-menu
                {:class (when @expanded? :is-active)}
                [:div.navbar-start
                 [nav-link "#/" "Home" :home]
                 [nav-link "#/about" "About" :about]
                 [nav-link "#/users" "Users" :users]
                 [nav-link "#/register" "Register" :register]
                 [nav-link "#/login" "Login" :login]
                 ]]
               [user-menu]]))
;;----------------------------
;;pages

(defn register-page []
  (reg/registration-form)
  )
(defn login-page []
  (l/login-form)
  )

(defn users-page []
  (u/user-table)
  )

(defn about-page []
  [:div
   [:p "Welcome to about page"
  [:section.section>div.container>div.content]]
   [:img {:src "/img/warning_clojure.png"}]])

(defn home-page []
  [:section.section>div.container>div.content
   (when-let [docs @(rf/subscribe [:docs])]
     [:div {:dangerouslySetInnerHTML {:__html (md->html docs)}}])])

(defn modal []
  (when-let [session-modal (session/get :modal)]
    [session-modal]))

(defn page []
  (if-let [page @(rf/subscribe [:common/page])]
    [:div
     [navbar]
    ; [modal]
     [page]]))

;;--------------------
;;router
(defn navigate! [match _]
  (rf/dispatch [:common/navigate match]))

(def router
  (reitit/router
    [["/" {:name        :home
           :view        #'home-page
           :controllers [{:start (fn [_] (rf/dispatch [:page/init-home]))}]}]
     ["/about" {:name :about
                :view #'about-page}]
     ["/users" {:name :users
                :view #'users-page}]
     ["/register" {:name :register
                :view #'register-page}]
     ["/login" {:name :login
                   :view #'login-page}]

     ]))

(defn start-router! []
  (rfe/start!
    router
    navigate!
    {}))

;; -------------------------
;; Initialize app
(defn ^:dev/after-load mount-components []
  (rf/clear-subscription-cache!)
  (rdom/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (start-router!)
  (ajx/load-interceptors!)
  ;(session/put! :identity js/identity)
  (mount-components))
