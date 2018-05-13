(ns table.table
  (:require [ccl.coordinates]))

(defn make_node_id
  [color label]
  (format "%d_%d" color label))

(defn build_node_info
  "Build a node id out of a color and a label.
  And also add coordinates into the node info"
  [color label x y]
  (let [id (make_node_id color label)]
    {:id id, :x x, :y y}))

(defn merge_one_color_at_coordinates
  "Get a label from ccl data using coordinates and put the label, a color
  and the coordinates into an accumulator"
  [data {:keys [x y] :as coord} color one_color_ccl_data]
  (let [label (get-in one_color_ccl_data [y x])
        id {:l label, :c color}
        val {:id id, :x x, :y y}]
    (if (pos-int? label)
      (assoc-in data [y x] val)
      data)))

(defn merge_one_color
  [{:keys [color width height data] :as acc} one_color_ccl_data]
  (let [coordinates (ccl.coordinates/generate_coordinates width height)
        new_data (reduce #(merge_one_color_at_coordinates %1 %2 color
                                                            one_color_ccl_data)
                         data coordinates)]
    (assoc acc
           :color (inc color)
           :data new_data)))

(defn merge_ccl_data
  "Get ccl arrays for different colors and merge them in a one array"
  [width height ccl_data]
  (let [color 0
        acc_data (ccl.ccl/init_2d_array width height)
        acc {:color color
             :width width
             :height height
             :data acc_data}
        {res_data :data} (reduce merge_one_color acc ccl_data)]
    res_data))

