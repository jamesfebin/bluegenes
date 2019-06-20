(ns bluegenes.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [re-frisk.core :refer [enable-re-frisk!]]
            [bluegenes.utils]
            [im-tables.core]
            [bluegenes.events]
            [bluegenes.subs]
            [bluegenes.route :as route]
            [bluegenes.views :as views]
            [bluegenes.config :as config]
            [bluegenes.pages.templates.core]
            [cljsjs.google-analytics]
            [oops.core :refer [oget oset! ocall oapply ocall! oapply!
                               oget+ oset!+ ocall+ oapply+ ocall!+ oapply!+]]))

; When compiled with Advanced Compilation, bluegenes.core/version will be replaced
; with (:version props) in project.clj
(goog-define version "dev")

;(defn dev-setup []
;  (when config/debug?
;    (devtools/install!)
;    (enable-re-frisk!)
;    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (route/init-routes!)
  (reagent/render [views/main-panel]
                  (ocall js/document "getElementById" "app")))

;; TODO replace or delete this function depending on needs with reitit
#_(defn navigate-to-deep-links []
    (let [url (oget js/window "location" "hash")
          hashless-path (last (clojure.string/split url #"#"))]
      (cond (> (count url) 2) ;; if there is more than #/ in the url, navigate there
            (if (= (first hashless-path) "/")
              (navigate! hashless-path)
              (navigate! (str "/" hashless-path))))))

(defn ^:export init [identity]
  (re-frame/dispatch-sync [:boot (js->clj identity :keywordize-keys true)])
  #_(navigate-to-deep-links)
  ;(dev-setup)
  ; Initialize our bootstrap dropdowns
  (ocall (js/$ ".dropdown-toggle") :dropdown)
  (mount-root))
