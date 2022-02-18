^{:nextjournal.clerk/visibility :hide}
(ns graph
  (:require [nextjournal.clerk :as clerk])
  (:import [java.time LocalDate]))

^{::clerk/viewer
  {:fetch-fn     (fn [_ x] x)
   :transform-fn (fn [{:as _x ::clerk/keys [var-from-def]}]
                   {:value @@var-from-def})
   :render-fn '(fn [{:as x :keys [var-name value options]}]
                 (v/html [:h1 (:text (:title value))]))}}
(defonce data (atom nil))

^::clerk/no-cache
(when-let [d @data]
  (clerk/vl d))

;; Datenquelle: Stadt Wien – https://data.wien.gv.at -
;; Datensatz: [Verkehrszählstellen Zählwerte Wien](https://www.data.gv.at/katalog/dataset/4707e82a-154f-48b2-864c-89fffc6334e1)

^::clerk/no-cache
(clerk/html
 [:p "Erstellt am " (str (LocalDate/now)) " von " [:a.text-blue.underline {:href "https://twitter.com/DieterKomendera"} "Dieter Komendera"]])
