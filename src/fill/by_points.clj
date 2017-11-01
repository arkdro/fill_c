(ns fill.by_points
  (:require [fill.plate :as pl]))

(defn cut_done?
  "Check if the begin point is not after the end point"
  [begin end]
  (pl/first_not_after_second? begin end))

(defn add_upper_node
  "If the color of the upper node is the target color
  then add that node to the queue"
  [node queue target_color plate]
  (if (pl/top_line? node plate)
    queue
    (let [upper_node (pl/upper_node node)]
      (if (pl/same_colors? upper_node target_color plate)
        (conj queue upper_node)
        queue))))

(defn add_lower_node
  "If the color of the lower node is the target color
  then add that node to the queue"
  [node queue target_color plate]
  (if (pl/bottom_line? node plate)
    queue
    (let [lower_node (pl/lower_node node)]
      (if (pl/same_colors? lower_node target_color plate)
        (conj queue lower_node)
        queue))))

(defn iterate_cut
  "Go through all the nodes from begin to end: set color, enqueue adjacent"
  [begin end queue target_color plate]
  (if (cut_done? begin end) [queue plate]
      (let [new_plate (pl/set_color begin target_color plate)
            queue2 (add_upper_node begin queue target_color plate)
            queue3 (add_lower_node begin queue2 target_color plate)]
        (recur (pl/next_node begin) end queue3 target_color new_plate))))

(defn take_item_out_of_queue
  "Take the item out of a queue. The queue must contain something"
  [queue]
  (let [item (peek queue)
        rest (pop queue)]
    [item rest]))

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

