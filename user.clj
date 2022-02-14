(require '[nextjournal.clerk :as clerk])

(comment
  (clerk/serve! {:browse? true})

  (clerk/show! "notebooks/dtv.clj")

  (clerk/serve! {:watch-paths ["notebooks" "src"]})

  (clerk/serve! {:watch-paths ["notebooks" "src"] :show-filter-fn #(clojure.string/starts-with? % "notebooks")})

  (clerk/build-static-app! {:paths ["notebooks/dtv.clj"]
                            :bundle? true}))

(comment
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
