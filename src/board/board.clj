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


(defn run
  [options]
  (graphics.bom/run options)
  )
