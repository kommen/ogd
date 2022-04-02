(ns ogd.nast
  (:require [clojure.java.io :as io]
            [clojure.data.csv :as csv]
            [nextjournal.clerk :as clerk])
  (:import (com.microsoft.playwright Playwright BrowserType$LaunchOptions Locator$SelectOptionOptions)
           (com.microsoft.playwright.options SelectOption LoadState)))

(def base-url "https://www.nast.at/charts/jahresauswertung/neubaugrtel")


(def context
  (let [pw      (Playwright/create)
        opts    (doto (BrowserType$LaunchOptions.)
                  (.setHeadless true))
        browser (.. pw webkit (launch opts))]
    (.. browser newContext)))

^{::clerk/viewer :table}
(def counting-points
  "Available traffic counting points"
  (with-open [page (.. context newPage)]
    (.navigate page base-url)
    (doall
     (for [cp-handle (.elementHandles (.locator page "a[href*='jahresauswertung']"))
           :let [name (.innerText cp-handle)
                 url  (.getAttribute cp-handle "href")]
           :when (not= name "JAHRESAUSWERTUNG")]
       {:name name :url url}))))

(defn year->rows
  "Fetches traffic data rows per year using the provided page"
  [page year-option]
  ;; selecting the option in the select will trigger an XHR to fetch data
  (.selectOption (.locator page "[name='select_date']")
                 (.getAttribute year-option "value"))
  ;; wait for data to arrive, bad, but works
  (Thread/sleep 500)
  (let [[head & rows] (.evaluate page "chart_data")]
    (->> rows
         (map (fn [row] (concat [(first head)] row)))
         (partition-by second) ;; by month
         (mapcat (fn [month-rows]
                   (map-indexed
                    (fn [i r] (conj r (inc i))) ;; conj the day of the month
                    month-rows))))))

(defn counting-point->rows
  "Fetches all available traffic data for a counting point"
  [cp]
  (with-open [page (.. context newPage)]
    (.navigate page (:url cp))
    (doall
     (for [year-option (.elementHandles (.locator page "[name='select_date'] > option"))
           row (year->rows page year-option)]
       row))))

^{::clerk/viewer :table}
(def result
  (for [cp counting-points
        cp-data (counting-point->rows cp)]
    (conj cp-data (:name cp))))

(comment
  (with-open [writer (io/writer "radverkehr.csv")]
    (csv/write-csv writer result)))

(comment
  (def cp (.. context newPage))
  (.navigate cp base-url)
  (for [year (.elementHandles (.locator cp "[name='select_date'] > option"))
        row (year->rows cp year)]
    row)

  (.selectOption (.locator cp "[name='select_date']")
                 (.setLabel (SelectOption.) "2021"))
  (.evaluate cp "chart_data")
  (.evaluate cp "options"))
