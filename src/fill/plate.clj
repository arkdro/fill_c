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
  ([node old_color plate]
   (let [color (get_color node plate)]
     (= color old_color))))

(def different_colors?
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
        (set_color_in_data 0 0 new_color data))
      data)))

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

(defn first_after_second?
  "Check if the first point is after the second"
  [{x1 :x} {x2 :x}]
  (> x1 x2))

(defn get_upper_node
  "Get coordinates of the upper node, if possible"
  [node plate]
  (if (not (top_line? node plate))
    (upper_node node)))

(defn get_lower_node
  "Get coordinates of the lower node, if possible"
  [node]
  (if (not (bottom_line? node))
    (lower_node node)))

(defn get_right_node
  "Get coordinates of the right node, if possible"
  [node plate]
  (if (not (end_of_line? node plate))
    (right_node node)))

(defn get_left_node
  "Get coordinates of the left node, if possible"
  [node]
  (if (not (beginning_of_line? node))
    (left_node node)))

(defn get_smaller_adjacent_coordinates
  "Get coordinates of adjacent nodes from smaller sides"
  [node plate]
  (let [;; upper (get_upper_node node plate)
        lower (get_lower_node node)
        ;; right (get_right_node node plate)
        left (get_left_node node)
        non_nil (filter #(some? %) [lower left])]
    non_nil))

(defn choose_node_to_duplicate_aux
  [nodes]
  (let [len (count nodes)
        idx (rand-int len)]
    (nth nodes idx)))

(defn choose_node_to_duplicate
  "Choose one of nodes to duplicate"
  [nodes]
  (if (seq nodes) (choose_node_to_duplicate_aux nodes)
      nil))

(defn duplicate_point
  [plate source target]
  (let [color (get_color source plate)]
    (set_color target color plate)))

(defn duplicate_point_with_probability
  "Copy color from a source node to a target node with some probability"
  [probability plate source target]
  (let [x (rand-int 100)]
    (if (> x probability) plate
        (duplicate_point plate source target))))

(defn duplicate_adjacent_point
  "Duplicate some adjacent point using probability"
  [probability plate target]
  (let [adjacent (get_smaller_adjacent_coordinates target plate)
        source (choose_node_to_duplicate adjacent)]
    (if (some? source) (duplicate_point_with_probability probability
                                                         plate source target)
        plate)))

(defn increase_domains_aux
  [width height data probability]
  (let [plate {:width width
               :height height
               :data data}
        coords (for [y (range height) x (range width)] {:x x, :y y})
        new_plate (reduce
                  #(duplicate_adjacent_point probability %1 %2)
                  plate coords)
        new_data (get new_plate :data)]
    new_data))

(defn increase_domains
  "Iterate over all points of data and duplicate an adjacent one
  in dependence of probability"
  [width height data probability]
  (cond
    (<= probability 0) data
    (> probability 100) data
    :default (increase_domains_aux width height data probability)))

(defn build_plate
  "Build a plate filled with colored points"
  [{width :width
    height :height
    color_range :color_range}
   {:keys [probability] :as opts}]
  (let [data (doall (vec (repeatedly height #(build_one_line width color_range))))
        data_increased_domains (increase_domains width height data probability)
        data2 (make_different_starts color_range width height data)
        cur1 (get_color_in_data 0 0 data)
        cur2 (get_color_in_data (dec width) (dec height) data)]
    {:width width
     :height height
     :color_range color_range
     :total_size (* width height)
     :size1 1
     :size2 1
     :cur1 cur1
     :cur2 cur2
     :data data}))

