(ns fill.request
  (:require [fill.by_points]
            [fill.plate])
  (:require [cheshire.core :as che])
  (:require [clojure.java.io]))

(defn build_one_step
  "Build one step for a request"
  [width height color_range]
  (let [x (rand-int width)
        y (rand-int height)
        color (rand-int color_range)
        point {:x x, :y y}]
    {:point point
     :color color}))

(defn generate_amount_of_steps
  "Generate amount of steps: 80% N, 10% 2*N, 10% 4*N"
  [width height color_range]
  (let [max (+ width height color_range)
        choice (rand-int 100)
        multiplier (cond
                     (< choice 80) 1
                     (< choice 90) 2
                     :default 4)]
    (rand-int (* multiplier max))))

(defn build_steps
  "Build steps for a request"
  [width height color_range]
  (let [amount (generate_amount_of_steps width height color_range)]
    (repeatedly amount #(build_one_step width height color_range))))

(defn apply_one_step
  "Apply one step to a plate"
  [plate {node :point
          color :color}]
  (fill.by_points/fill node color plate))

(defn build_expected_plate
  "Build the expected plate by applying steps to the input plate"
  [steps plate]
  (reduce apply_one_step plate steps))

(defn generate_one_request
  "Generate one request for a fill"
  [width height color_range & [json_opts]]
  (let [plate (fill.plate/build_plate {:color_range color_range
                                       :width width
                                       :height height})
        steps (build_steps width height color_range)
        expected (build_expected_plate steps plate)
        request {:steps steps
                 :input_data plate
                 :expected_data expected}]
    (che/generate-string request json_opts)))

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

(defn generate_requests
  "Create a bunch of request files in the specified directory"
  [dir amount width height color_range]
  (dotimes [idx amount]
    (let [request (generate_one_request width height color_range)]
      (write_request dir idx request))))

(defn generate_request
  "Create a request file in the specified directory"
  [dir idx width height color_range json_opts]
  (let [request (generate_one_request width height color_range json_opts)]
    (write_request dir idx request)))

