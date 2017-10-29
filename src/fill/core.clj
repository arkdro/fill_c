(ns fill.core
  (:gen-class))

(defn get_random_color
  "Get random color"
  [color_range]
  (rand-int color_range))

(defn run
  "Get params and run fill"
  [color_range width height]
  (let [data (create_data color_range width height)
        plate {:color_range color_range
               :width width
               :height height
               :data data}
        node {:x 0
              :y 0}
        new_color (get_random_color color_range)]
    (fill.by_points/fill node new_color plate)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
