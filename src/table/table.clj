(ns table.table)

(defn merge_one_color_over_coordinates
  "Get a label from ccl data using coordinates and put the label
  and a color into an accumulator"
  [data {:keys [x y] :as coord} color one_color_ccl_data]
  (let [label (get-in [y x] one_color_ccl_data)
        val {:l label, :c color}]
    (assoc-in data [y x] val)))

(defn merge_one_color
  [{:keys [color width height data] :as acc} one_color_ccl_data]
  (let [coordinates (ccl.ccl/generate_coordinates width height)
        new_data (reduce #(merge_one_color_over_coordinates %1 %2 color
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

