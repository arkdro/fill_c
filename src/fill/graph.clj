(ns fill.graph
  (:require [ccl.coordinates]))

;; A node:
;; id
;; neigbour nodes
;; cells

(defn fresh_node
  [id]
  {:id id
   :cells #{}
   :neigbours #{}})

(defn add_same_id_cells_to_node
  "Add cells with the same node id to a node"
  [{:keys [cells] :as node} cells_to_add]
  (let [only_coordinates (map #(select-keys % [:x :y]) cells_to_add)
        new_cells (apply conj cells only_coordinates)]
    (assoc node :cells new_cells)))

(defn add_different_ids_to_node
  "Add cells with diffrent node ids as neigbours"
  [{:keys [neigbours] :as node} cells_to_add]
  (let [ids (map #(get % :id) cells_to_add)
        new_neigbours (apply conj neigbours ids)]
    (assoc node :neigbours new_neigbours)))

(defn add_neigbours_to_node
  "Add cells with the same node id to the current node.
  Add cells with diffrent node ids as neigbours."
  [node same_id different_ids]
  (-> node
      (add_same_id_cells_to_node same_id)
      (add_different_ids_to_node different_ids)))

(defn add_one_neigbour_to_graph
  "Add the current id to the neigbours of a node and then add this updated node
  to the accumulator"
  [acc {:keys [id]} current_id]
  (let [{:keys [neigbours] :as node} (get acc id (fresh_node id))
        new_neigbours (conj neigbours current_id)
        new_node (assoc node :neigbours new_neigbours)]
    (assoc acc id new_node)))

(defn add_neigbours_to_graph
  "Add the id to neigbour nodes and then add these updated neigbour nodes
  to the accumulator"
  [acc id neigbours]
  (reduce #(add_one_neigbour_to_graph %1 %2 id) acc neigbours))

(defn get_node_id_by_cell
  [{:keys [x y]} ccl_data]
  (get-in ccl_data [y x :id]))

(defn get_node
  "Fetch a node from the accumulator by id.
  Return default in case of a new node"
  [acc id]
  (get acc id (fresh_node id)))

(defn put_node
  [acc id node]
  (assoc acc id node))

(defn get_one_neigbour
  [ccl_data {:keys [x y]}]
  (get-in ccl_data [y x]))

(defn get_neigbour_coordinates
  [coord width connectivity]
  (ccl.coordinates/get_mask_coordinates coord width connectivity))

(defn- same_id?
  [id {neigbour_id :id}]
  (= id neigbour_id))

(defn get_neigbours
  [id coord width connectivity ccl_data]
  (let [coordinates (get_neigbour_coordinates coord width connectivity)
        valid (filter some? coordinates)
        nodes (map #(get_one_neigbour ccl_data %) valid)
        {same_id true
         different_ids false} (group-by #(same_id? id %) nodes)]
    (replace {nil []} [same_id different_ids])))

(defn process_one_cell
  "Add a cell and its neigbours to a graph"
  [acc {:keys [x y] :as coord} width connectivity ccl_data]
  (let [id (get_node_id_by_cell coord ccl_data)
        [same_id different_ids] (get_neigbours id coord width connectivity
                                               ccl_data)
        node (get_node acc id)
        node_at_current_coord (assoc coord :id id)
        same_id_all (conj same_id node_at_current_coord)
        new_node (add_neigbours_to_node node same_id_all different_ids)
        acc2 (add_neigbours_to_graph acc id different_ids)
        acc3 (put_node acc2 id new_node)]
    acc3))

(defn build_graph
  "Build a graph from merged ccl data"
  [width height connectivity ccl_data]
  (let [
        coordinates (ccl.coordinates/generate_coordinates width height)
        acc {}
        ]
    (reduce #(process_one_cell %1 %2 width connectivity ccl_data)
            acc coordinates)
    )
  )
