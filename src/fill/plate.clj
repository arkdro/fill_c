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

(defn same_colors?
  "Compare two colors"
  [c1 c2]
  (= c1 c2))

(def not_same_colors?
  (complement same_colors?))

