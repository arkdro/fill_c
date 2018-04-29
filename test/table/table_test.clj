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
               [0 {:c 3, :l 1} 0]]]
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
          exp_data [[1 2 {:c 5, :l 2} 4]
                    [5 {:c 5, :l 1} 7 {:c 5, :l 2}]
                    [{:c 5, :l 1} 10 {:c 5, :l 1} {:c 5, :l 1}]]
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
          exp [[{:c 3, :l 1} {:c 2, :l 1} {:c 2, :l 1} {:c 2, :l 1}
                {:c 2, :l 1} {:c 2, :l 1} {:c 3, :l 2} {:c 3, :l 2}
                {:c 3, :l 2}]
               [{:c 3, :l 1} {:c 2, :l 1} {:c 2, :l 1} {:c 0, :l 1}
                {:c 0, :l 1} {:c 2, :l 1} {:c 2, :l 1} {:c 2, :l 1}
                {:c 1, :l 1}]
               [{:c 0, :l 1} {:c 0, :l 1} {:c 0, :l 1} {:c 3, :l 3}
                {:c 0, :l 1} {:c 1, :l 1} {:c 1, :l 1} {:c 1, :l 1}
                {:c 1, :l 1}]
               [{:c 2, :l 2} {:c 0, :l 1} {:c 0, :l 1} {:c 3, :l 3}
                {:c 0, :l 1} {:c 3, :l 4} {:c 1, :l 1} {:c 3, :l 5}
                {:c 0, :l 3}]]
          ]
      (is (= act exp)))))
