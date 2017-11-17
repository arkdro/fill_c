(ns fill.generator)

(defn random_in_range
  "Generate random number between min (inclusive) and max (exclusive)"
  [min max]
  (let [delta (- max min)
        rand (rand-int delta)]
    (+ min rand)))

(defn generate_width
  "Generate random width"
  [min max]
  (random_in_range min max))

(defn generate_height
  "Generate random height"
  [min max]
  (random_in_range min max))

(defn generate_color_range
  "Generate random color range"
  [min max]
  (random_in_range min max))

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

