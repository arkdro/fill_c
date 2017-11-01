(ns fill.by_points
  (:require [fill.plate :as pl]))

;; TODO remove duplicated code in 'find_xxxxx_of_cut'
(defn find_begin_of_cut
  "Decrease the coordinate until the color of the node no longer matches
  the target color"
  [node target_color plate]
  (cond
    (pl/beginning_of_line? node) node
    (pl/not_same_colors? node target_color plate) node
    :default (recur (pl/previous_node node) target_color plate)))

(defn find_end_of_cut
  "Increase the coordinate until the color of the node no longer matches
  the target color"
  [node target_color plate]
  (cond
    (pl/end_of_line? node plate) node
    (pl/not_same_colors? node target_color plate) node
    :default (recur (pl/next_node node) target_color plate)))

(defn process_one_node
  "Extract one node from a queue and do the filling for it"
  [queue target_color new_color plate]
  (assert false "not implemented"))

(defn step
  "One step of filling"
  [queue
   target_color
   new_color
   {:keys [width height data] :as plate}]
  (if (empty? queue) plate
      (let [[new_queue new_plate] (process_one_node queue
                                                    target_color
                                                    new_color
                                                    plate)]
        (recur new_queue target_color new_color new_plate))))

(defn fill
  "Flood fill"
  [node new_color plate]
  (let [target_color (pl/get_color node plate)]
    (cond
      (pl/same_colors? target_color new_color) plate
      (pl/not_same_colors? (pl/get_color node plate) target_color) plate
      :default (let [queue (conj (clojure.lang.PersistentQueue/EMPTY) node)]
                 (step queue target_color new_color plate)))))

