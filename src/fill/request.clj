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
  ""
  [dir amount width height color_range]
  )

