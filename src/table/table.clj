(ns table.table)

(defn merge_one_color
  [acc ccl_data]
  )

(defn merge_ccl_data
  "Get ccl arrays for different colors and merge them in a one array"
  [width height ccl_data]
  (let [color 0
        acc_data (ccl.ccl/init_2d_array width height)
        acc {:color color
             :data acc_data}
        {res_data :data} (reduce merge_one_color acc ccl_data)]
    res_data))

