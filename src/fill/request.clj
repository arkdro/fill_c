(ns fill.request)

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

