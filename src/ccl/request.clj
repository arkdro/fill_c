(ns ccl.request
  (:require [fill.plate]
            [fill.graph]
            [ccl.ccl]
            [table.table])
  (:require [cheshire.core :as che])
  (:require [clojure.java.io]))

(defn generate_json_string
  "Generate json string using provided options"
  [request {pretty :pretty}]
  (let [json_opts {:pretty pretty}]
    (che/generate-string request json_opts)))

(defn generate_expected_data
  [width height color_range {connectivity :connectivity} data]
  (ccl.ccl/ccl_multi_color width height color_range connectivity data))

(defn replace_background_points_one_row
  [ccl-output-background row]
  (replace {:no ccl-output-background} row))

(defn replace_background_points_one_item
  [ccl-output-background item]
  (mapv #(replace_background_points_one_row ccl-output-background %) item))

(defn replace_background_points
  [expected {:keys [ccl-output-background]}]
  (mapv #(replace_background_points_one_item ccl-output-background %) expected))

(defn generate_one_request
  "Generate one ccl request"
  [width height color_range opts]
  (let [plate (fill.plate/build_plate {:color_range color_range
                                       :width width
                                       :height height}
                                      opts)
        data (get plate :data)
        expected (generate_expected_data width height color_range opts
                                         data)
        expected2 (replace_background_points expected opts)
        connectivity (get opts :connectivity)
        merged_ccl_data (table.table/merge_ccl_data width height expected)
        graph (fill.graph/build_graph width height connectivity merged_ccl_data)
        request {:input_data plate
                 :expected_data expected2
                 :expected_merged_ccl_data merged_ccl_data
                 :expected_graph graph}]
    (generate_json_string request opts)))

(defn build_file_name
  "Create a file name using a dir and a sequence number"
  [dir idx]
  (let [file (format "req-%08d.json" idx)]
    (.getPath (clojure.java.io/file dir file))))

(defn write_request
  "Write a request to a file"
  [dir idx request]
  (let [filename (build_file_name dir idx)]
    (spit filename request)))

(defn generate_request
  "Create a request file in the specified directory"
  [dir idx width height color_range opts]
  (let [request (generate_one_request width height color_range opts)]
    (write_request dir idx request)))

