(ns ccl.ccl
  "Connected component labeling. Based on 'A linear-time two-scan labeling
  algorithm' by He, Chao, Suzuki."
  )

(defn init_tables
  "Generate initial tables for intermediate data"
  [width height]
  (let [result_labels (for [y (range height) x (range width)] :no)
        len (* width height)
        repr_tab (for [i (range len)] :no)
        next_label (for [i (range len)] :no)
        tail (for [i (range len)] :no)]
    {:repr_tab repr_tab
     :next_label next_label
     :result_labels result_labels
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

(defn get_mask_coordinates
  "Get coordinates of mask points for the specified coordinates.
  It uses 8-connectivity."
  [coord width]
  (let [left (get_left_coord coord)
        upper_left (get_upper_left_coord coord)
        upper (get_upper_coord coord)
        upper_right (get_upper_right_coord coord width)
        coordinates [upper_left upper upper_right left]]
    (map some? coordinates)))

(defn get_mask_colors
  "Get colors for the mask at the specified coordinates"
  [coord width data]
  (let [coordinates (get_mask_coordinates coord width)]
    (map #(get_color data %) coordinates)))

(defn background_mask?
  "Check if a surrounding mask for a point at coordinates have background color"
  [coord width color data]
  (let [mask_colors (get_mask_colors coord width data)]
    (cond
      (= [] mask_colors) true
      (every? #(not= color %) mask_colors) true
      :default false)))

(defn assign_new_label
  "Assign a new label to a point"
  [{:keys [result_labels repr_tab next_label tail m] :as acc}
   {:keys [x y]}]
  (let [new_result_labels (assoc-in result_labels [y x] m)
        new_repr_tab (assoc repr_tab m m)
        new_next_label (assoc next_label m :last)
        new_tail (assoc tail m m)
        new_m (inc m)]
    (-> acc
        (assoc :result_labels new_result_labels)
        (assoc :repr_tab new_repr_tab)
        (assoc :next_label new_next_label)
        (assoc :tail new_tail)
        (assoc :m new_m))))

(defn get_label
  "Get the result label of a point with coordinates"
  [result_labels {:keys [x y]}]
  (get-in result_labels [y x]))

(defn get_mask_labels
  "Get existing labels for points in mask"
  [coord width result_labels]
  (let [coordinates (get_mask_coordinates coord width)]
    (map #(get_label result_labels %) coordinates)))

(defn get_min_label
  "Get minimal label for points of a mask"
  [coord {:keys [width result_labels]}]
  (let [labels (get_mask_labels coord width result_labels)]
    ;; FIXME crash on empty seq. E.g. when coord = (0, 0)
    (apply min labels)))

(defn provisional_labels_equal?
  [cur_label mask_label]
  (= cur_label mask_label))

(defn pass1_step
  "Do one step of the first pass"
  [color data
   {:keys [width] :as acc}
   coord]
  (cond
    (background_point? color data coord) acc
    (background_mask? color width data coord) (assign_new_label acc coord)
    :default (assign_minimal_label acc coord)))

(defn pass1
  "Pass 1"
  [width height color data]
  (let [tables (init_tables width height)
        acc (-> tables
                (assoc :m 1)
                (assoc :width width)
                (assoc :height height))
        coordinates (for [y (range height) x (range width)] {:x x, :y y})]
    (reduce #(pass1_step color data %1 %2) acc coordinates)))

(defn ccl
  "Do CCL"
  [width height color data]
  
  )

