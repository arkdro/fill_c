(ns fill.by_points
  (:require [fill.plate :as pl]))

(defn cut_done?
  "Check if the begin point is after the end point"
  [begin end]
  (pl/first_after_second? begin end))

(defn add_upper_node
  "If the color of the upper node is the target color
  then add that node to the queue"
  [node queue old_color plate]
  (if (pl/top_line? node plate)
    queue
    (let [upper_node (pl/upper_node node)]
      (if (pl/same_colors? upper_node old_color plate)
        (conj queue upper_node)
        queue))))

(defn add_lower_node
  "If the color of the lower node is the target color
  then add that node to the queue"
  [node queue old_color plate]
  (if (pl/bottom_line? node)
    queue
    (let [lower_node (pl/lower_node node)]
      (if (pl/same_colors? lower_node old_color plate)
        (conj queue lower_node)
        queue))))

(defn iterate_cut
  "Go through all the nodes from begin to end: set color, enqueue adjacent"
  [begin end queue old_color new_color plate]
  (if (cut_done? begin end) [queue plate]
      (let [new_plate (pl/set_color begin new_color plate)
            queue2 (add_upper_node begin queue old_color plate)
            queue3 (add_lower_node begin queue2 old_color plate)
            next_node (pl/right_node begin)]
        (recur next_node end queue3 old_color new_color new_plate))))

(defn take_item_out_of_queue
  "Take the item out of a queue. The queue must contain something"
  [queue]
  (let [item (peek queue)
        rest (pop queue)]
    [item rest]))

;; TODO remove duplicated code in 'find_xxxxx_of_cut'
(defn- find_begin_of_cut_aux
  [prev_node node old_color plate]
  (cond
    (pl/different_colors? node old_color plate) prev_node
    (pl/beginning_of_line? node) node
    :default (recur node (pl/left_node node) old_color plate)))

(defn find_begin_of_cut
  "Decrease the coordinate until the color of the node no longer matches
  the target color"
  [node old_color plate]
  (find_begin_of_cut_aux node node old_color plate))

(defn- find_end_of_cut_aux
  [prev_node node old_color plate]
  (cond
    (pl/different_colors? node old_color plate) prev_node
    (pl/end_of_line? node plate) node
    :default (recur node (pl/right_node node) old_color plate)))

(defn find_end_of_cut
  "Increase the coordinate until the color of the node no longer matches
  the target color"
  [node old_color plate]
  (find_end_of_cut_aux node node old_color plate))

(defn process_one_node
  "Extract one node from a queue and do the filling for it"
  [queue old_color new_color plate]
  (let [[node shortened_queue] (take_item_out_of_queue queue)
        begin (find_begin_of_cut node old_color plate)
        end (find_end_of_cut node old_color plate)]
    (iterate_cut begin end shortened_queue old_color new_color plate)))

(defn step
  "One step of filling"
  [queue
   old_color
   new_color
   {:keys [width height data] :as plate}]
  (if (empty? queue) plate
      (let [[new_queue new_plate] (process_one_node queue
                                                    old_color
                                                    new_color
                                                    plate)]
        (recur new_queue old_color new_color new_plate))))

(defn fill
  "Flood fill"
  [node new_color plate]
  (let [old_color (pl/get_color node plate)]
    (cond
      (pl/same_colors? old_color new_color) plate
      (pl/different_colors? (pl/get_color node plate) old_color) plate ; :-/
      :default (let [queue (conj (clojure.lang.PersistentQueue/EMPTY) node)]
                 (step queue old_color new_color plate)))))

