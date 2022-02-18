(require
 '[clojure.string :as str]
 '[nextjournal.clerk :as clerk]
 '[nextjournal.clerk.view :as view])

(comment
  (clerk/serve! {:browse? true})

  (clerk/show! "notebooks/dtv.clj")

  (clerk/serve! {:watch-paths ["notebooks" "src"]})

  (clerk/serve! {:watch-paths ["notebooks" "src"] :show-filter-fn #(clojure.string/starts-with? % "notebooks")})

  (clerk/build-static-app! {:paths ["notebooks/dtv.clj"]
                            :bundle? true}))

(defn gen-graph! [name data]
  (let [out-path (str "dtv/"
                      (-> name
                          (str/replace #"\s" "")
                          (str/replace #"/" "-")
                          str/lower-case))
        out-name (str "public/" out-path ".html")
        notebook "notebooks/graph.clj"]
    (reset! graph/data data)
    (spit out-name
          (view/->static-app
           {:bundle? false
            :current-path notebook
            :path-prefix "ogd/dtv"
            :path->url {notebook out-name
                        "" "/index.html"}
            :path->doc (hash-map notebook (clerk/file->viewer "notebooks/graph.clj"))}))))

(comment
  (doseq [[name data] (take 2 dtv/counting-points)]
    (gen-graph! name data))

  (doseq [[name data] dtv/counting-points]
    (gen-graph! name data))

  (doseq [[name data] dtv/counting-points]
    (reset! graph/data data)
    (clerk/build-static-app!
     {:paths ["notebooks/graph.clj"]
      :out-path (str "public/build/" (clojure.string/replace name #"\s" ""))
      :bundle? true
      :browse? false}))

  (reset! dtv/select-state (first (keys dtv/counting-points)))
  (clerk/build-static-app!
   {:paths ["notebooks/dtv.clj"]
    :out-path (str "public/build/" (clojure.string/replace @dtv/select-state #"\s" ""))
    :bundle? true
    :browse? false})



  (reset! graph/data (val (first dtv/counting-points))))
