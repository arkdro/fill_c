(ns fill.plate)

(defn get_color
  "Get the color of a node in a plate"
  [{:keys [x y] :as node}
   {:keys [data] :as plate}]
  (get-in data [y x]))

(defn same_colors?
  "Compare two colors"
  [c1 c2]
  (= c1 c2))

(def not_same_colors?
  (complement same_colors?))

