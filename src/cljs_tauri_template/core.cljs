(ns cljs-tauri-template.core
  (:require
   ["@tauri-apps/api/tauri" :refer [invoke]]
   [reagent.core :as r]
   [reagent.dom :as rdom]))

(defonce greeting (r/atom nil))

(defn greet [text]
  #(-> (invoke "greet" (clj->js {:name @text}))
       (.then (fn [result]
                (reset! greeting result)))))

(defn welcome []
  (r/with-let [text (r/atom "")]
    [:div
     [:h1 "Welcome!"]
     [:h4 "CLJS + Tauri"]
     [:div
      [:label {:for "greet"} "Greet: "]
      [:input {:id "greet" :on-change #(reset! text (.-value (.-target %))) :value @text}]
      [:button {:on-click (greet text)} "Send"]]
     (when @greeting
       [:p "From rust: " @greeting])]))

(defn- ^:dev/after-load render []
  (rdom/render [welcome] (.getElementById js/document "app")))

(.addEventListener js/window "DOMContentLoaded" render)
