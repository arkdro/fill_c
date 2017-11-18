(ns fill.core
  (:require [clojure.tools.cli :refer [parse-opts]])
  (:require [fill.by_points]
            [fill.generator]
            [fill.plate])
  (:gen-class))

(defn get_random_color
  "Get random color"
  [color_range]
  (rand-int color_range))

(def cli-options
  ;; An option with a required argument
  [[nil "--min-width N" "minimal width"
    :default 4
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 %) "Must be bigger than 0"]]
   [nil "--max-width N" "maximal width"
    :default 10
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 %) "Must be bigger than 0"]]
   [nil "--min-height N" "minimal height"
    :default 4
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 %) "Must be bigger than 0"]]
   [nil "--max-height N" "maximal height"
    :default 10
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 %) "Must be bigger than 0"]]
   [nil "--min-color-range N" "minimal color range"
    :default 4
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 %) "Must be bigger than 0"]]
   [nil "--max-color-range N" "maximal color range"
    :default 10
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 %) "Must be bigger than 0"]]
   ["-n" "--num-request N" "number of requests"
    :default 1
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 %) "Must be bigger than 0"]]
   ["-d" "--dir DIR" "output directory for requests"
    :default "."
    :validate [string? "Must be string"]]

   ;; A non-idempotent option
   ["-v" nil "Verbosity level"
    :id :verbosity
    :default 0
    :assoc-fn (fn [m k _] (update-in m [k] inc))]
   ;; A boolean option defaulting to nil
   ["-p" "--pretty"]
   ["-h" "--help"]])

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
  "Parse command line arguments and run the generator"
  [& args]
  (let [opts (parse-opts args cli-options)
        options (get opts :options)
        help (get-in opts [:options :help])]
    (if help
      (println (get opts :summary))
      (fill.generator/generate-requests options))))

