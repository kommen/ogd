(ns ogd.utils)

(defn string->stream
  ([s] (string->stream s "UTF-8"))
  ([s encoding]
   (-> s
       (.getBytes encoding)
       (java.io.ByteArrayInputStream.))))

(def month-str->int
  {"JAN." 1 "FEB." 2 "MÄRZ" 3 "APRIL" 4 "MAI" 5 "JUNI" 6
   "JULI" 7 "AUG." 8 "SEP." 9 "OKT" 10 "NOV" 11 "DEZ." 12})

(defn point->latlng [shape-str]
  (let [[lng lat] (rest (re-matches #"POINT \((.*?) (.*?)\)" shape-str))]
    {:lat (parse-double lat) :lng (parse-double lng)}))
