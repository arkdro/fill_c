(ns fill.plate)

;; 4-way connectivity. Used by get upper/lower node.
;; Coordinates increase:
;; x - left to right
;; y - bottom to up

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

(defn set_color
  "Set the color of a point at the specified location in a plate"
  [{:keys [x y]}
   color
   {:keys [data] :as plate}]
  (let [new_data (set_color_in_data x y color data)]
    (assoc plate :data new_data)))

(defn same_colors?
  "Compare two colors"
  ([c1 c2]
   (= c1 c2))
  ([node target_color plate]
   (let [color (get_color node plate)]
     (= color target_color))))

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

(defn build_plate
  "Build a plate filled with colored points"
  [{width :width
    height :height
    color_range :color_range}]
  (let [data (doall (vec (repeatedly height #(build_one_line width color_range))))
        data2 (make_different_starts color_range width height data)
        cur1 (get_color_in_data 0 0 width height data)
        cur2 (get_color_in_data (dec width) (dec height) width height data)]
    {:width width
     :height height
     :color_range color_range
     :total_size (* width height)
     :size1 1
     :size2 1
     :cur1 cur1
     :cur2 cur2
     :data data}))

(defn beginning_of_line?
  "Check if the node is at the beginning of a line"
  [{x :x}]
  (<= x 0))

(defn end_of_line?
  "Check if the node is at the end of a line"
  [{x :x}
   {width :width}]
  (>= x (dec width)))

(defn top_line?
  "Check if the node is at the top of a plate"
  [{y :y}
   {height :height}]
  (>= y (dec height)))

(defn bottom_line?
  "Check if the node is at the bottom of a plate"
  [{y :y}]
  (<= y 0))

(defn left_node
  "Decrease the x coordinate of a node"
  [node]
  (update node :x dec))

(defn right_node
  "Increase the x coordinate of a node"
  [node]
  (update node :x inc))

(defn upper_node
  "Increase the y coordinate of a node"
  [node]
  (update node :y inc))

(defn lower_node
  "Decrease the y coordinate of a node"
  [node]
  (update node :y dec))

(defn first_not_after_second?
  "Check if the first point is not after the second"
  [{x1 :x} {x2 :x}]
  (<= x1 x2))

