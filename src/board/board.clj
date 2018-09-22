(ns board.board
  "Create a board based on parameters"
  (:require
   [ccl.request]
   [graphics.bom]
   ))

(defn get_node
  "Get a node id from merged ccl data at given coordinates"
  [merged_data {x :x, y :y}]
  (get-in merged_data [y x :id]))

(defn get_color
  "Get a color from input data at given coordinates"
  [data {x :x, y :y}]
  (get-in data [y x]))

(defn match_color?
  [colors_by_ids color id]
  (= (get colors_by_ids id) color))

(defn split_neighbours_by_color
  "Partition neighbours by color into matching and non-matching nodes"
  [{:keys [data merged_data] :as board} neighbours color]
  (let [colors_by_ids (build_neighbour_colors data merged_data neighbours)
        {matching true
         non_matching false} (group-by #(match_color? colors_by_ids color %)
                                       neighbours)]
    [matching non_matching]))


(defn run
  [options]
  (graphics.bom/run options)
  )
