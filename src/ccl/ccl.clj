(ns ccl.ccl
  "Connected component labeling. Based on 'A linear-time two-scan labeling
  algorithm' by He, Chao, Suzuki."
  )

(defn init_tables
  "Generate initial tables for intermediate data"
  [width height]
  (let [repr_tab (for [y (range height) x (range width)] :no)
        len (* width height)
        next_label (for [i (range len)] :no)
        tail (for [i (range len)] :no)]
    {:repr_tab repr_tab
     :next_label next_label
     :tail tail}))

(defn get_color
  "Get the color of a point in a data"
  [{:keys [x y]} data]
  (get-in [y x] data))

(defn ccl
  "Do CCL"
  [width height color data]
  
  )

