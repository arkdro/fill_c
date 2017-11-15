(ns fill.request)

(defn build_one_step
  "Build one step for a request"
  [width height color_range]
  (let [x (rand-int width)
        y (rand-int height)
        color (rand-int color_range)
        point {:x x, :y y}]
    {:point point
     :color color}))

(defn build_steps
  "Build steps for a request"
  [width height color_range]
  (let [amount (Math/round (* (+ width height) 0.5))]
    (repeatedly amount #(build_one_step width height color_range))))

(defn build_expected_plate
  ""
  [steps plate]
  plate)

(defn generate_one_request
  "Generate one request for a fill"
  [width height color_range]
  (rand-int color_range)
  )

(defn write_request
  "Write a request to a file"
  [dir idx request]
  (println dir idx request)
  )

(defn generate_requests
  "Create a bunch of request files in the specified directory"
  [dir amount width height color_range]
  (dotimes [idx amount]
    (let [request (generate_one_request width height color_range)]
      (write_request dir idx request))))

