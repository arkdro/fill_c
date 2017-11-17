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

(defn generate_one_request
  "Generate one randomized request"
  [idx
   {:keys [min-width max-width
           min-height max-height
           min-color-range max-color-range
           dir]
    :as opts}]
  (let [width (generate_width min-width max-width)
        height (generate_height min-height max-height)
        color_range (generate_color_range min-color-range max-color-range)]
    (fill.request/generate_request dir idx width height color_range)))

(defn generate-requests
  "Generate randomized requests"
  [{amount :num-request :as opts}]
  (dotimes [idx amount]
    (generate_one_request idx opts)))

