^{:nextjournal.clerk/visibility :hide}
(ns graph
  (:require [nextjournal.clerk :as clerk]))

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

;; Quelle: https://www.data.gv.at/katalog/dataset/4707e82a-154f-48b2-864c-89fffc6334e1
