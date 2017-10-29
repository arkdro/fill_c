(ns fill.plate)

(defn get_color_in_data
  "Get the color of a raw data at the specified location"
  [x y data]
  (get-in data [y x]))

(defn get_color
  "Get the color of a node in a plate"
  [{:keys [x y]}
   {:keys [data]}]
  (get_color_in_data x y data))

(defn set_color_in_data
  "Set the color of a point at the specified location"
  [x y color data]
  (assoc-in data [y x] color))

(defn same_colors?
  "Compare two colors"
  [c1 c2]
  (= c1 c2))

(def not_same_colors?
  (complement same_colors?))

(defn build_one_point
  "Build one point"
  [color_range]
  (rand-int color_range))

(defn build_one_line
  "Build one line"
  [width color_range]
  (vec (repeatedly width #(build_one_point color_range))))

(defn make_different_starts
  "Make sure that two starting points are different"
  [color_range width height data]
  (let [cur1 (get_color_in_data 0 0 data)
        cur2 (get_color_in_data (dec width) (dec height) data)]
    (if (same_colors? cur1 cur2)
      (let [new_color (mod (inc cur1) color_range)] ; might give some edge to one side
        (set_color_in_data 0 0 new_color data)))))

