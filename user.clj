(require '[nextjournal.clerk :as clerk])

(comment
  (clerk/serve! {:browse? true})

  (clerk/show! "notebooks/dtv.clj")

  (clerk/serve! {:watch-paths ["notebooks" "src"]})

  (clerk/serve! {:watch-paths ["notebooks" "src"] :show-filter-fn #(clojure.string/starts-with? % "notebooks")})

  (clerk/build-static-app! {:paths ["notebooks/dtv.clj"] :bundle? true}))
