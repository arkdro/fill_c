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

(defn foreground_point?
  "Check if a point at coordinates have foreground color"
  [coord color data]
  (let [data_color (get_color coord data)]
    (= color data_color)))

(def background_point?
  (complement foreground_point?))

(defn get_left_coord
  "Get coordinates of a point on the left"
  [{:keys [x y]}]
  (if (= x 0) nil
      {:x (dec x)
       :y y}))

(defn get_upper_coord
  "Get coordinates of a point on the up"
  [{:keys [x y]}]
  (if (= y 0) nil
      {:x x
       :y (dec y)}))

(defn get_upper_left_coord
  "Get coordinates of a point on the upper-left"
  [{:keys [x y]}]
  (cond (= x 0) nil
        (= y 0) nil
        :default {:x (dec x)
                  :y (dec y)}))

(defn get_upper_right_coord
  "Get coordinates of a point on the upper-right"
  [{:keys [x y]} width]
  (cond (>= x (- width 1)) nil
        (= y 0) nil
        :default {:x (inc x)
                  :y (dec y)}))

(defn ccl
  "Do CCL"
  [width height color data]
  
  )

