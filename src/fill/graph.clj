(ns fill.graph)

;; A node:
;; id
;; neigbour nodes
;; cells

(defn add_neigbours_to_node
  "Add cells with the same node id to the current node.
  Add cells with diffrent node ids as neigbours."
  [node same_id different_ids])

(defn add_neigbours_to_graph
  "For each of nodes: add the id as a neigbour node and update the accumulator"
  [acc id different_ids])

(defn process_one_cell
  "Add a cell and its neigbours to a graph"
  [acc {:keys [x y] :as coord}]
  (let [id (get_node_id_by_cell coord ccl_data)
        [same_id different_ids] (get_neigbours id coord ccl_data)
        node (get_node acc id)
        new_node (add_neigbours_to_node node same_id different_ids)
        acc2 (add_neigbours_to_graph acc id different_ids)
        acc3 (put_node acc2 id node)
        ]
    acc3
    )
  )

(defn build_graph
  "Build a graph from merged ccl data"
  []
  (let [
        coordinates (generate_coordinates )
        acc {}
        ]
    (reduce process_one_cell acc coordinates)
    )
  )
