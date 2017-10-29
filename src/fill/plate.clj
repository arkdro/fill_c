(ns fill.plate)

(defn get_color
  "Get the color of a node in a plate"
  [{:keys [x y] :as node}
   {:keys [data] :as plate}]
  (get-in data [y x]))

