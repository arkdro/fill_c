(ns fill.by_points
  (:require [fill.plate]))

(defn step
  "One step of filling"
  [queue
   target_color
   new_color
   {:keys [width height data] :as plate}]
  (assert false "not implemented"))

(defn fill
  "Flood fill"
  [node new_color plate]
  (let [target_color (fill.plate/get_color node plate)]
    (cond
      (fill.plate/same_colors? target_color new_color) plate
      (fill.plate/not_same_colors? (fill.plate/get_color node plate) target_color) plate
      :default (let [queue (conj (clojure.lang.PersistentQueue/EMPTY) node)]
                 (step queue target_color new_color plate)))))

