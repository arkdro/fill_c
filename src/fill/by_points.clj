(ns fill.by_points
  (:require [fill.plate :as pl]))

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
  (let [target_color (pl/get_color node plate)]
    (cond
      (pl/same_colors? target_color new_color) plate
      (pl/not_same_colors? (pl/get_color node plate) target_color) plate
      :default (let [queue (conj (clojure.lang.PersistentQueue/EMPTY) node)]
                 (step queue target_color new_color plate)))))

