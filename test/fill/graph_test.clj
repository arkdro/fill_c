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

