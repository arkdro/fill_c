(ns fill.generator)

(defn generate-requests
  "Generate requests"
  [{:keys [min-width max-width
           min-height max-height
           min-color-range max-color-range
           num-request]
    :as opts}]
  (if (> num-request 0)
    (let [
          width ()
          new_opts (update opts :num-request dec)
          ]
      (recur new_opts))))

