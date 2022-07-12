^{:nextjournal.clerk/visibility :hide}
(ns index
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]
   [nextjournal.clerk :as clerk])
  (:import [java.time LocalDate]))

;;# Übersicht Dauerzählstellen der Stadt Wien
^{::clerk/viewer clerk/hide-result}
(defn path->title [f]
  (let [path (str (.relativize (.toPath (io/file "public/dtv")) (.toPath f)))]
    (when-let [name (second (re-matches #"(.*)-([\d]*)\.html" path))]
      [:a {:href path}
       (interleave (map str/capitalize (str/split name #"-"))
                   (repeat " "))])))


(clerk/html
 (into [:ul.font-sans]
       (comp
        (map path->title)
        (filter some?)
        (map (fn [f] [:li f])))
       (sort (filter #(.isFile %) (file-seq (io/file "public/dtv"))))))


(clerk/html
 [:div.text-xs.font-mono.text-slate-500
  [:p
   "Datenquelle: Stadt Wien – "
   [:a {:href "https://data.wien.gv.at"} "data.wien.gv.at"] " | "
   "Datensatz: "
   [:a {:href "https://www.data.gv.at/katalog/dataset/4707e82a-154f-48b2-864c-89fffc6334e1" }
    "Verkehrszählstellen Zählwerte Wien"]]
  [:p "Erstellt am " (str (LocalDate/now)) " von " [:a.text-blue.underline {:href "https://twitter.com/DieterKomendera"} "Dieter Komendera"]]])
