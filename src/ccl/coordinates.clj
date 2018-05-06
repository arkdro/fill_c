(ns ccl.coordinates)

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

(defn get_mask_coordinates_8
  "Get coordinates of mask points for the specified coordinates.
  It uses 8-connectivity."
  [coord width]
  (let [left (get_left_coord coord)
        upper_left (get_upper_left_coord coord)
        upper (get_upper_coord coord)
        upper_right (get_upper_right_coord coord width)
        coordinates [upper_left upper upper_right left]]
    (filter some? coordinates)))

(defn get_mask_coordinates_6_even
  "Get coordinates of mask points for the specified coordinates,
  when the y-coordinate is even.
  It uses 6-connectivity."
  [coord width]
  (let [left (get_left_coord coord)
        upper (get_upper_coord coord)
        upper_right (get_upper_right_coord coord width)
        coordinates [upper upper_right left]]
    (filter some? coordinates)))

(defn get_mask_coordinates_6_odd
  "Get coordinates of mask points for the specified coordinates,
  when the y-coordinate is odd.
  It uses 6-connectivity."
  [coord width]
  (let [left (get_left_coord coord)
        upper_left (get_upper_left_coord coord)
        upper (get_upper_coord coord)
        coordinates [upper_left upper left]]
    (filter some? coordinates)))

(defn get_mask_coordinates_6
  [{:keys [y] :as coord}
   width]
  (if (even? y)
    (get_mask_coordinates_6_even coord width)
    (get_mask_coordinates_6_odd coord width)))

(defn get_mask_coordinates
  [coord width connectivity]
  (case connectivity
    4 (ex-info "Connectivity 4 is not implemented" {})
    6 (get_mask_coordinates_6 coord width)
    8 (get_mask_coordinates_8 coord width)))

(defn generate_coordinates
  "Generate x, y coordinates from [0, 0] to (width, height)."
  [width height]
  (for [y (range height) x (range width)] {:x x, :y y}))

