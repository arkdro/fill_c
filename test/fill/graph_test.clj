(ns fill.graph_test
  (:require [clojure.test :refer :all]
            [fill.graph :refer :all]))

(deftest add_same_id_cells_to_node_test
  (testing "Add cells with the same node id to a node"
    (let [node {:id 1
                :cells #{{:x 0, :y 0}}}
          cells_to_add [{:x 0, :y 0, :id 1}
                        {:x 0, :y 1, :id 1}]
          exp {:id 1
               :cells #{{:x 0, :y 0}
                        {:x 0, :y 1}}}
          act (add_same_id_cells_to_node node cells_to_add)]
      (is (= act exp)))))

(deftest add_different_ids_to_node_test
  (testing "Add cells with diffrent node ids as neigbours"
    (let [node {:id 1
                :neigbours #{1}}
          cells_to_add [{:x 0, :y 0, :id 1}
                        {:x 0, :y 1, :id 2}]
          exp {:id 1
               :neigbours #{1 2}}
          act (add_different_ids_to_node node cells_to_add)]
      (is (= act exp)))))

(deftest add_neigbours_to_node_test
  (testing "Add neigbours to a node"
    (let [node {:id 1
                :cells #{{:x 2, :y 1}}
                :neigbours #{5}}
          same_id [{:x 0, :y 0, :id 1}
                   {:x 1, :y 1, :id 1}]
          different_ids [{:x 3, :y 0, :id 2}
                         {:x 3, :y 1, :id 3}
                         {:x 3, :y 1, :id 2}]
          exp {:id 1
               :cells #{{:x 2, :y 1}
                        {:x 0, :y 0}
                        {:x 1, :y 1}}
               :neigbours #{2 3 5}}
          act (add_neigbours_to_node node same_id different_ids)]
      (is (= act exp)))))

(deftest add_one_neigbour_to_graph_test
  (testing "Add one neigbour to the graph"
    (let [id 1
          node {:id id
                :cells #{{:x 2, :y 1}}
                :neigbours #{5}}
          current_id 2
          acc {id node}
          exp_node {:id 1
                    :cells #{{:x 2, :y 1}}
                    :neigbours #{2 5}}
          exp {id exp_node}
          act (add_one_neigbour_to_graph acc node current_id)]
      (is (= act exp)))))

(deftest add_one_neigbour_to_graph_test2
  (testing "Add one neigbour to the graph, 2"
    (let [
          node1 {:id 1
                :cells #{{:x 1, :y 0}}
                :neigbours #{}}
          node2 {:id 2
                :cells #{{:x 2, :y 1}}
                :neigbours #{5}}
          current_id 5
          acc {2 node2}
          exp {2 node2
               1 {:id 1
                  :cells #{}
                  :neigbours #{5}}}
          act (add_one_neigbour_to_graph acc node1 current_id)]
      (is (= act exp)))))

(deftest add_neigbours_to_graph_test
  (testing "Add neigbours to the graph"
    (let [id 1
          node {:id id
                :cells #{{:x 2, :y 1}}
                :neigbours #{5}}
          acc {id node}
          neigbours [{:x 3, :y 1, :id 2}
                     {:x 2, :y 2, :id 3}]
          exp {1 {:id 1
                  :cells #{{:x 2, :y 1}}
                  :neigbours #{5}}
               2 {:id 2
                  :cells #{}
                  :neigbours #{1}}
               3 {:id 3
                  :cells #{}
                  :neigbours #{1}}
               }
          act (add_neigbours_to_graph acc id neigbours)]
      (is (= act exp)))))

(deftest get_node_id_by_cell_test
  (testing "get node id by cell"
    (let [node {:x 0
                :y 1}
          ccl_data [[0 1 0]
                    [{:id 2} 0 1]
                    [0 0 1]]
          exp 2
          act (get_node_id_by_cell node ccl_data)]
      (is (= act exp)))))

