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
           dir num-request]
    :as opts}]
  (if (> num-request 0)
    (let [width (generate_width min-width max-width)
          height (generate_height min-height max-height)
          color_range (generate_color_range min-color-range max-color-range)
          opts2 (update opts :num-request dec)
          idx (get opts :idx 0)
          new_opts (assoc opts :idx (inc idx))]
      (fill.request/generate_request dir idx width height color_range)
      (recur new_opts))))

