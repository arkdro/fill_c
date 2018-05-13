(ns table.table_test
  (:require [clojure.test :refer :all]
            [table.table :refer :all]))

(deftest merge_one_color_at_coordinates_test
  (testing "merge_one_color_at_coordinates"
    (let [data [[0 0 0]
                [0 0 0]
                [0 0 0]]
          coord {:x 1, :y 2}
          color 3
          one_color_ccl_data [[0 2 0]
                              [0 0 2]
                              [0 1 1]]
          act (merge_one_color_at_coordinates data coord color
                                              one_color_ccl_data)
          exp [[0 0 0]
               [0 0 0]
               [0 {:id {:c 3, :l 1}, :x 1, :y 2} 0]]]
      (is (= act exp)))))

(deftest merge_one_color_test
  (testing "merge_one_color"
    (let [data [[1 2 3 4]
                [5 6 7 8]
                [9 10 11 12]]
          color 5
          width 4
          height 3
          one_color_ccl_data [[0 0 2 0]
                              [0 1 0 2]
                              [1 0 1 1]]
          acc {:width width
               :height height
               :color color
               :data data}
          act (merge_one_color acc one_color_ccl_data)
          exp_data [[1 2 {:id {:c 5, :l 2}, :x 2, :y 0} 4]
                    [5 {:id {:c 5, :l 1}, :x 1, :y 1}
                     7 {:id {:c 5, :l 2}, :x 3, :y 1}]
                    [{:id {:c 5, :l 1}, :x 0, :y 2}
                     10
                     {:id {:c 5, :l 1}, :x 2, :y 2}
                     {:id {:c 5, :l 1}, :x 3, :y 2}]]
          exp {:width width
               :height height
               :color (inc color)
               :data exp_data}
          ]
      (is (= act exp)))))

(deftest merge_ccl_data_test
  (testing "merge_ccl_data"
    (let [width 9
          height 4
          data [[3 2 2 2 2 2 3 3 3]
                [3 2 2 0 0 2 2 2 1]
                [0 0 0 3 0 1 1 1 1]
                [2 0 0 3 0 3 1 3 0]]
          ccl_data [[[:no :no :no :no :no :no :no :no :no]
                     [:no :no :no 1 1 :no :no :no :no]
                     [1 1 1 :no 1 :no :no :no :no]
                     [:no 1 1 :no 1 :no :no :no 3]]
                    [[:no :no :no :no :no :no :no :no :no]
                     [:no :no :no :no :no :no :no :no 1]
                     [:no :no :no :no :no 1 1 1 1]
                     [:no :no :no :no :no :no 1 :no :no]]
                    [[:no 1 1 1 1 1 :no :no :no]
                     [:no 1 1 :no :no 1 1 1 :no]
                     [:no :no :no :no :no :no :no :no :no]
                     [2 :no :no :no :no :no :no :no :no]]
                    [[1 :no :no :no :no :no 2 2 2]
                     [1 :no :no :no :no :no :no :no :no]
                     [:no :no :no 3 :no :no :no :no :no]
                     [:no :no :no 3 :no 4 :no 5 :no]]]
          act (merge_ccl_data width height ccl_data)
          exp [[{:id {:c 3, :l 1} :x 0 :y 0}
                {:id {:c 2, :l 1} :x 1 :y 0}
                {:id {:c 2, :l 1} :x 2 :y 0}
                {:id {:c 2, :l 1} :x 3 :y 0}
                {:id {:c 2, :l 1} :x 4 :y 0}
                {:id {:c 2, :l 1} :x 5 :y 0}
                {:id {:c 3, :l 2} :x 6 :y 0}
                {:id {:c 3, :l 2} :x 7 :y 0}
                {:id {:c 3, :l 2} :x 8 :y 0}]
               [{:id {:c 3, :l 1} :x 0 :y 1}
                {:id {:c 2, :l 1} :x 1 :y 1}
                {:id {:c 2, :l 1} :x 2 :y 1}
                {:id {:c 0, :l 1} :x 3 :y 1}
                {:id {:c 0, :l 1} :x 4 :y 1}
                {:id {:c 2, :l 1} :x 5 :y 1}
                {:id {:c 2, :l 1} :x 6 :y 1}
                {:id {:c 2, :l 1} :x 7 :y 1}
                {:id {:c 1, :l 1} :x 8 :y 1}]
               [{:id {:c 0, :l 1} :x 0 :y 2}
                {:id {:c 0, :l 1} :x 1 :y 2}
                {:id {:c 0, :l 1} :x 2 :y 2}
                {:id {:c 3, :l 3} :x 3 :y 2}
                {:id {:c 0, :l 1} :x 4 :y 2}
                {:id {:c 1, :l 1} :x 5 :y 2}
                {:id {:c 1, :l 1} :x 6 :y 2}
                {:id {:c 1, :l 1} :x 7 :y 2}
                {:id {:c 1, :l 1} :x 8 :y 2}]
               [{:id {:c 2, :l 2} :x 0 :y 3}
                {:id {:c 0, :l 1} :x 1 :y 3}
                {:id {:c 0, :l 1} :x 2 :y 3}
                {:id {:c 3, :l 3} :x 3 :y 3}
                {:id {:c 0, :l 1} :x 4 :y 3}
                {:id {:c 3, :l 4} :x 5 :y 3}
                {:id {:c 1, :l 1} :x 6 :y 3}
                {:id {:c 3, :l 5} :x 7 :y 3}
                {:id {:c 0, :l 3} :x 8 :y 3}]]
          ]
      (is (= act exp)))))

(deftest build_node_info_test
  (testing "build_node_info"
    (let [color 31
          label 1234
          x 2
          y 5
          act (build_node_info color label x y)
          exp {:id "31_1234" :x 2 :y 5}]
      (is (= act exp)))))

