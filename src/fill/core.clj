(ns fill.core
  (:require [fill.by_points]
            [fill.plate])
  (:gen-class))

(defn get_random_color
  "Get random color"
  [color_range]
  (rand-int color_range))

(defn run
  "Get params and run fill"
  [color_range width height]
  (let [plate (fill.plate/build_plate {:color_range color_range
                                       :width width
                                       :height height})
        node {:x 0
              :y 0}
        new_color (fill.plate/build_one_point color_range)]
    (fill.by_points/fill node new_color plate)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
