(ns board.board
  "Create a board based on parameters"
  (:require
   [ccl.request]
   [graphics.bom]
   ))

(defn get_node_from_graph
  [graph id]
  (get graph id))

(defn get_node
  "Get a node id from merged ccl data at given coordinates"
  [merged_data {x :x, y :y}]
  (get-in merged_data [y x :id]))

(defn get_color
  "Get a color from input data at given coordinates"
  [data {x :x, y :y}]
  (get-in data [y x]))

(defn get_node_coordinates
  [graph id]
  (let [cells (get-in graph [id :cells])]
    (first cells)))

(defn add_one_node_to_color_mapping
  [data graph acc id]
  (let [coord (get_node_coordinates graph id)
        color (get_color data coord)]
    (assoc acc id color)))

(defn build_neighbour_colors
  "Build a map of id -> color for given nodes"
  [data graph nodes]
  (reduce #(add_one_node_to_color_mapping data graph %1 %2) {} nodes))

(defn match_color?
  [colors_by_ids color id]
  (= (get colors_by_ids id) color))

(defn split_neighbours_by_color
  "Partition neighbours by color into matching and non-matching nodes"
  [{:keys [data graph] :as board} neighbours color]
  (let [colors_by_ids (build_neighbour_colors data graph neighbours)
        {matching true
         non_matching false} (group-by #(match_color? colors_by_ids color %)
                                       neighbours)]
    [matching non_matching]))

(defn get_one_next_hop_neighbours
  [graph acc id]
  (let [node (get_node_from_graph graph id)
        neighbours (get :neighbours node)]
    (clojure.set/union acc neighbours)))

(defn get_next_hop_neighbours
  [{:keys [graph]} neighbours]
  (reduce #(get_one_next_hop_neighbours graph %1 %2) #{} neighbours))

(defn run_color
  [board {:keys [neighbours] :as party} new_color]
  (let [[matching non_matching] (split_neighbours_by_color board
                                                           neighbours
                                                           new_color)
        next_hop_neighbours (get_next_hop_neighbours board matching)
        merged_neighbours (merge_neighbours non_matching next_hop_neighbours)
        ;; removing nodes which are already consumed by any party
        new_neighbours (remove_consumed_neighbours merged_neighbours)
        new_consumed_nodes (update_consumed_nodes consumed_nodes matching)
        new_board (update_board board matching owner)]
    [new_board new_party]))

(defn prepare_party
  "Use a start node as a clean start"
  [owner start {:keys [data merged_data] :as board}]
  (let [node (get_node merged_data start)
        initial_party {:neighbours #{node}
                       :consumed_nodes #{}
                       :color nil
                       :id owner}]
    (run_color board initial_party (get_color data start))))

(defn prepare_state
  [data merged_data graph]
  (let [board {:width width
               :height height
               :data data
               :merged_data merged_data
               :graph graph}
        owner1 :p1
        owner2 :p2
        start1 {:x 0, :y 0}
        start2 {:x (dec width), :y (dec height)}
        [board1 party1] (prepare_party owner1 start1 board)
        [board2 party2] (prepare_party owner2 start2 board1)
        state {:board board2
               :party1 party1
               :party2 party2}]
    state))

(defn run
  [options]
  (graphics.bom/run options)
  )
