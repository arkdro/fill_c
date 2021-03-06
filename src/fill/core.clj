(ns fill.core
  (:require [clojure.tools.cli :refer [parse-opts]])
  (:require [fill.by_points]
            [fill.generator]
            [fill.plate]
            [ccl.generator])
  (:gen-class))

(defn get_random_color
  "Get random color"
  [color_range]
  (rand-int color_range))

(def cli-options
  ;; An option with a required argument
  [["-t" "--type T" "type of request: fill or ccl"
    :validate [#(or (= "fill" %) (= "ccl" %)) "Must be fill or ccl"]]
   [nil "--ccl-output-background STRING"
    "Default string for background in ccl output"
    :parse-fn #(read-string %)
    :default "."]
   [nil "--min-width N" "minimal width"
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
   [nil "--probability N" "probability of duplicating adjacent color, 0...100"
    :default 0
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 %) "Must not be smaller than 0"
               #(> 100 %) "Must not be bigger than 100"]]
   ["-n" "--num-request N" "number of requests"
    :default 1
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 %) "Must be bigger than 0"]]
   ["-c" "--connectivity N" "connectivity: 4, 6, 8"
    :default 8
    :parse-fn #(Integer/parseInt %)
    :validate [#(contains? #{4 6 8} %) "Must be one of these: 4, 6, 8"]]
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
        type (get options :type)
        errors (get opts :errors)
        help (get-in opts [:options :help])]
    (cond
      help (println (get opts :summary))
      errors (println errors)
      (= type "fill") (fill.generator/generate-requests options)
      (= type "ccl") (ccl.generator/generate-requests options)
      :default (println (get opts :summary)))))

