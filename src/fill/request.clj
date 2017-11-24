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

(defn duplicate_step
  "Build a step with the same coordinates, but the different color"
  [color_range
   {color :color :as point}]
  (let [new_color (rand-int color_range)]
    (if (= color new_color) (duplicate_step color_range point)
        (assoc point :color new_color))))

(defn generate_amount_of_steps
  "Generate amount of steps: 80% N, 10% 2*N, 10% 4*N"
  [width height color_range]
  (let [max (+ width height color_range)
        choice (rand-int 100)
        multiplier (cond
                     (< choice 90) 1
                     (< choice 97) 2
                     :default 4)]
    (rand-int (* multiplier max))))

(defn add_duplicated_step
  "Add a duplicated step to the accumulator"
  [acc color_range]
  (let [step (first acc)
        new_step (duplicate_step color_range step)]
    (cons new_step acc)))

(defn add_new_step
  "Add a new step to the accumulator"
  [acc width height color_range]
  (let [step (build_one_step width height color_range)]
    (cons step acc)))

(defn add_step
  "Add a step (new or duplicated) to the accumulator"
  [acc width height color_range]
  (let [repeat (rand-int 100)]
    (if (and (seq acc)
             (< repeat 20)) (add_duplicated_step acc color_range)
        (add_new_step acc width height color_range))))

(defn build_steps_aux
  ""
  [i acc width height color_range]
  (if (<= i 0) acc
      (let [new_acc (add_step acc width height color_range)]
        (recur (dec i) new_acc width height color_range))))

(defn build_steps
  "Build steps for a request"
  [width height color_range]
  (let [amount (generate_amount_of_steps width height color_range)]
    (build_steps_aux amount '() width height color_range)))

(defn apply_one_step
  "Apply one step to a plate"
  [plate {node :point
          color :color}]
  (fill.by_points/fill node color plate))

(defn build_expected_plate
  "Build the expected plate by applying steps to the input plate"
  [steps plate]
  (reduce apply_one_step plate steps))

(defn generate_json_string
  "Generate json string using provided options"
  [request {pretty :pretty}]
  (let [json_opts {:pretty pretty}]
    (che/generate-string request json_opts)))

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
    (generate_json_string request json_opts)))

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

