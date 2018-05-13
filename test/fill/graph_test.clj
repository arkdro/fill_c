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

(deftest get_one_neigbour_test
  (testing "get node id by cell"
    (let [node {:x 0
                :y 1}
          ccl_data [[0 1 0]
                    [{:id 2} 0 1]
                    [0 0 1]]
          exp {:id 2}
          act (get_one_neigbour ccl_data node)]
      (is (= act exp)))))

(deftest get_neigbours_test1
  (testing "get neigbours, 1"
    (let [id 2
          coord {:x 0
                 :y 1}
          width 3
          connectivity 6
          ccl_data [[{:id 1} {:id 3} {:id 6}]
                    [{:id 2} {:id 5} {:id 4}]
                    [{:id 4} {:id 7} {:id 8}]]
          exp_same_ids []
          exp_different_ids [{:id 1}]
          exp [exp_same_ids exp_different_ids]
          act (get_neigbours id coord width connectivity ccl_data)]
      (is (= act exp)))))

(deftest get_neigbours_test2
  (testing "get neigbours, 2"
    (let [id 3
          coord {:x 1
                 :y 1}
          width 3
          connectivity 6
          ccl_data [[{:id 5} {:id 3, :x 1, :y 0} {:id 1}]
                    [{:id 2} {:id 3} {:id 4}]
                    [{:id 4} {:id 1} {:id 3}]]
          exp_same_ids [{:x 1, :y 0, :id 3}]
          exp_different_ids [{:id 5} {:id 2}]
          exp [exp_same_ids exp_different_ids]
          act (get_neigbours id coord width connectivity ccl_data)]
      (is (= act exp)))))

(deftest get_neigbours_test3
  (testing "get neigbours, 3"
    (let [id 2
          coord {:x 0
                 :y 1}
          width 3
          connectivity 8
          ccl_data [[{:id 1} {:id 3} {:id 8}]
                    [{:id 2} {:id 5} {:id 4}]
                    [{:id 4} {:id 7} {:id 6}]]
          exp_same_ids []
          exp_different_ids [{:id 1}, {:id 3}]
          exp [exp_same_ids exp_different_ids]
          act (get_neigbours id coord width connectivity ccl_data)]
      (is (= act exp)))))

(deftest get_neigbours_test4
  (testing "get neigbours, 4"
    (let [id 3
          coord {:x 1
                 :y 1}
          width 3
          connectivity 8
          ccl_data [[{:id 5} {:id 3, :x 1, :y 0} {:id 1}]
                    [{:id 2} {:id 3} {:id 4}]
                    [{:id 4} {:id 6} {:id 7}]]
          exp_same_ids [{:x 1, :y 0, :id 3}]
          exp_different_ids [{:id 5} {:id 1} {:id 2}]
          exp [exp_same_ids exp_different_ids]
          [act_same_ids act_different_ids] (get_neigbours id coord width
                                                          connectivity
                                                          ccl_data)]
      (is (= act_same_ids exp_same_ids))
      (is (= act_different_ids exp_different_ids)))))

(deftest process_one_cell_test1
  (testing "process one cell, 1"
    (let [acc {3 {:id 3,
                  :cells #{{:x 1, :y 0}}
                  :neigbours #{5}}}
          coord {:x 1
                 :y 1}
          width 3
          connectivity 6
          ccl_data [[{:id 5, :x 0, :y 0} {:id 3, :x 1, :y 0} {:id 1}]
                    [{:id 2} {:id 3} {:id 4}]
                    [{:id 4} {:id 6} {:id 7}]]
          exp {3 {:id 3,
                  :cells #{{:x 1, :y 0}
                           {:x 1, :y 1}}
                  :neigbours #{2 5}
                  }
               2 {:id 2
                  :cells #{}
                  :neigbours #{3}}
               5 {:id 5
                  :cells #{}
                  :neigbours #{3}}
               }
          act (process_one_cell acc coord width connectivity ccl_data)
          ]
      (is (= act exp)))))

(deftest process_one_cell_test2
  (testing "process one cell, 2"
    (let [acc {3 {:id 3,
                  :cells #{{:x 1, :y 0}}
                  :neigbours #{5}}}
          coord {:x 0
                 :y 1}
          width 3
          connectivity 6
          ccl_data [[{:id 5, :x 0, :y 0} {:id 3, :x 1, :y 0} {:id 1}]
                    [{:id 2} {:id 3} {:id 4}]
                    [{:id 4} {:id 6} {:id 7}]]
          exp {3 {:id 3,
                  :cells #{{:x 1, :y 0}}
                  :neigbours #{5}
                  }
               2 {:id 2
                  :cells #{{:x 0, :y 1}}
                  :neigbours #{5}}
               5 {:id 5
                  :cells #{}
                  :neigbours #{2}}}
          act (process_one_cell acc coord width connectivity ccl_data)]
      (is (= act exp)))))

(deftest process_one_cell_test3
  (testing "process one cell, 3"
    (let [acc {3 {:id 3,
                  :cells #{{:x 1, :y 0}}
                  :neigbours #{5}}}
          coord {:x 1
                 :y 1}
          width 3
          connectivity 8
          ccl_data [[{:id 5} {:id 3, :x 1, :y 0} {:id 1}]
                    [{:id 2} {:id 3} {:id 4}]
                    [{:id 4} {:id 6} {:id 7}]]
          exp {3 {:id 3,
                  :cells #{{:x 1, :y 0}
                           {:x 1, :y 1}}
                  :neigbours #{1 2 5}
                  }
               1 {:id 1
                  :cells #{}
                  :neigbours #{3}}
               2 {:id 2
                  :cells #{}
                  :neigbours #{3}}
               5 {:id 5
                  :cells #{}
                  :neigbours #{3}}
               }
          act (process_one_cell acc coord width connectivity ccl_data)
          ]
      (is (= act exp)))))

(deftest process_one_cell_test4
  (testing "process one cell, 4"
    (let [acc {3 {:id 3,
                  :cells #{{:x 1, :y 0}}
                  :neigbours #{5}}}
          coord {:x 0
                 :y 1}
          width 3
          connectivity 8
          ccl_data [[{:id 5, :x 0, :y 0} {:id 3, :x 1, :y 0} {:id 1}]
                    [{:id 2} {:id 3} {:id 4}]
                    [{:id 4} {:id 6} {:id 7}]]
          exp {3 {:id 3,
                  :cells #{{:x 1, :y 0}}
                  :neigbours #{2 5}
                  }
               2 {:id 2
                  :cells #{{:x 0, :y 1}}
                  :neigbours #{3 5}}
               5 {:id 5
                  :cells #{}
                  :neigbours #{2}}}
          act (process_one_cell acc coord width connectivity ccl_data)]
      (is (= act exp)))))

(deftest build_graph_test1
  (testing "build graph, 1"
    (let [width 3
          height 3
          connectivity 6
          ccl_data [[{:id 5 :x 0 :y 0} {:id 3 :x 1 :y 0} {:id 1 :x 2 :y 0}]
                    [{:id 2 :x 0 :y 1} {:id 3 :x 1 :y 1} {:id 4 :x 2 :y 1}]
                    [{:id 4 :x 0 :y 2} {:id 6 :x 1 :y 2} {:id 7 :x 2 :y 2}]]
          exp {1 {:id 1
                  :cells #{{:x 2, :y 0}}
                  :neigbours #{3 4}}
               2 {:id 2
                  :cells #{{:x 0, :y 1}}
                  :neigbours #{3 4 5}}
               3 {:id 3
                  :cells #{{:x 1, :y 0}
                           {:x 1, :y 1}}
                  :neigbours #{1 2 4 5 6}}
               4 {:id 4
                  :cells #{{:x 2, :y 1}
                           {:x 0, :y 2}}
                  :neigbours #{1 2 3 6 7}}
               5 {:id 5
                  :cells #{{:x 0, :y 0}}
                  :neigbours #{2 3}}
               6 {:id 6
                  :cells #{{:x 1, :y 2}}
                  :neigbours #{3 4 7}}
               7 {:id 7
                  :cells #{{:x 2, :y 2}}
                  :neigbours #{4 6}}}
          act (build_graph width height connectivity ccl_data)]
      (is (= act exp)))))

(deftest build_graph_test2
  (testing "build graph, 2"
    (let [width 3
          height 3
          connectivity 8
          ccl_data [[{:id 5 :x 0 :y 0} {:id 3 :x 1 :y 0} {:id 1 :x 2 :y 0}]
                    [{:id 2 :x 0 :y 1} {:id 3 :x 1 :y 1} {:id 4 :x 2 :y 1}]
                    [{:id 4 :x 0 :y 2} {:id 6 :x 1 :y 2} {:id 7 :x 2 :y 2}]]
          exp {1 {:id 1
                  :cells #{{:x 2, :y 0}}
                  :neigbours #{3 4}}
               2 {:id 2
                  :cells #{{:x 0, :y 1}}
                  :neigbours #{3 4 5 6}}
               3 {:id 3
                  :cells #{{:x 1, :y 0}
                           {:x 1, :y 1}}
                  :neigbours #{1 2 4 5 6 7}}
               4 {:id 4
                  :cells #{{:x 2, :y 1}
                           {:x 0, :y 2}}
                  :neigbours #{1 2 3 6 7}}
               5 {:id 5
                  :cells #{{:x 0, :y 0}}
                  :neigbours #{2 3}}
               6 {:id 6
                  :cells #{{:x 1, :y 2}}
                  :neigbours #{2 3 4 7}}
               7 {:id 7
                  :cells #{{:x 2, :y 2}}
                  :neigbours #{3 4 6}}}
          act (build_graph width height connectivity ccl_data)]
      (is (= act exp)))))

