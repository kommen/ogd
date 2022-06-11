(require
 '[clojure.string :as str]
 '[nextjournal.clerk :as clerk]
 '[nextjournal.clerk.view :as view])

(comment
  (clerk/serve! {:browse? true})

  (clerk/show! "notebooks/dtv.clj")

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
            :path->doc (hash-map notebook (clerk/file->viewer notebook))}))))


(comment

  (do
    ;; get some data into the graph
    (reset! graph/data (rand-nth (vals dtv/counting-points)))

    (clerk/recompute!))

  ;; only some
  (doseq [[name data] (take 2 dtv/counting-points)]
    (gen-graph! name data))

  ;; gen all the graphs
  (doseq [[name data] dtv/counting-points]
    (gen-graph! name data))



  (reset! dtv/select-state (first (keys dtv/counting-points)))
  )
