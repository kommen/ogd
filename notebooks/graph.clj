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


(clerk/html
 [:link {:rel "stylesheet"
         :href "https://unpkg.com/leaflet@1.7.1/dist/leaflet.css"
         :crossorigin ""}])

^{::clerk/viewer :hide-result}
(def leaflet
  {:fetch-fn (fn [_ x] x)
   :render-fn
   '(fn [value]
      (v/html
       (when-let [{:keys [lat lng]} value]
         [v/with-d3-require {:package ["leaflet@1.7.1/dist/leaflet.js"]}
          (fn [leaflet]
            [:div {:style {:height 400}
                   :ref
                   (fn [el]
                     (when el
                       (let [m                   (.map leaflet el (clj->js {:zoomControl false :zoomDelta 0.5 :zoomSnap 0.0}))
                             location-latlng     (.latLng leaflet lat lng)
                             location-marker     (.marker leaflet location-latlng)
                             basemap-hidpi-layer (.tileLayer leaflet
                                                             "https://{s}.wien.gv.at/basemap/bmaphidpi/normal/google3857/{z}/{y}/{x}.jpeg"
                                                             (clj->js
                                                              {:subdomains    ["maps" "maps1" "maps2" "maps3" "maps4"]
                                                               :maxZoom       25
                                                               :maxNativeZoom 19
                                                               :attribution   "basemap.at"
                                                               :errorTileUrl  "/transparent.gif"}))]
                         (.addTo basemap-hidpi-layer m)
                         (.addTo location-marker m)
                         (.setView m location-latlng 13.7))))}])])))})

;; ## Zählpunkt Standort

^::clerk/no-cache
(clerk/with-viewer leaflet
  (when-let [d @data]
    (:dtv/location d)))

^::clerk/no-cache
(clerk/html
 [:p "Erstellt am " (str (LocalDate/now)) " von " [:a.text-blue.underline {:href "https://twitter.com/DieterKomendera"} "Dieter Komendera"]])

;; Karte: https://basemap.at |
;; Datenquelle: Stadt Wien – https://data.wien.gv.at |
;; Datensatz: [Verkehrszählstellen Zählwerte Wien](https://www.data.gv.at/katalog/dataset/4707e82a-154f-48b2-864c-89fffc6334e1)
