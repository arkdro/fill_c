(ns fill.plate_test
  (:require [clojure.test :refer :all]
            [fill.plate :refer :all]))

(deftest get_color_in_data_test
  (testing "get color in data"
    (let [x 0
          y 1
          data [[1 3 5] [2 4 6] [10 11 12]]
          act (get_color_in_data x y data)
          exp 2]
      (is (= act exp)))))

(deftest get_color_test
  (testing "get color"
    (let [node {:x 2 :y 0}
          plate {:data [[1 3 5] [2 4 6] [10 11 12]]}
          act (get_color node plate)
          exp 5]
      (is (= act exp)))))

(deftest set_color_in_data_test
  (testing "set color in data"
    (let [x 2
          y 2
          color 23
          data [[1 3 5] [2 4 6] [10 11 12]]
          act (set_color_in_data x y color data)
          exp [[1 3 5] [2 4 6] [10 11 23]]]
      (is (= act exp)))))

(deftest set_color_test
  (testing "set color"
    (let [node {:x 2 :y 1}
          color 23
          plate {:data [[1 3 5] [2 4 6] [10 11 12]]}
          act (set_color node color plate)
          exp {:data [[1 3 5] [2 4 23] [10 11 12]]}]
      (is (= act exp)))))

(deftest same_colors_test1
  (testing "same color?/2, 2"
    (let [color1 1
          color2 2
          act (same_colors? color1 color2)
          exp false]
      (is (= act exp)))))

(deftest same_colors_test2
  (testing "same color?/2"
    (let [color1 1
          color2 1
          act (same_colors? color1 color2)
          exp true]
      (is (= act exp)))))

(deftest same_colors_test3
  (testing "same color?/3"
    (let [node {:x 1, :y 2}
          old_color 2
          plate {:data [[1 3 5] [2 4 6] [10 11 12]]}
          act (same_colors? node old_color plate)
          exp false]
      (is (= act exp)))))

(deftest same_colors_test4
  (testing "same color?/3"
    (let [node {:x 1, :y 2}
          old_color 11
          plate {:data [[1 3 5] [2 4 6] [10 11 12]]}
          act (same_colors? node old_color plate)
          exp true]
      (is (= act exp)))))

(deftest different_colors_test1
  (testing "not same color?/3"
    (let [node {:x 1, :y 2}
          old_color 2
          plate {:data [[1 3 5] [2 4 6] [10 11 12]]}
          act (different_colors? node old_color plate)
          exp true]
      (is (= act exp)))))

(deftest make_different_starts_test
  (testing "make different starts"
    (let [color_range 12
          width 3
          height 3
          data [[11 3 5] [2 4 6] [10 9 11]]
          act (make_different_starts color_range width height data)
          exp [[0 3 5] [2 4 6] [10 9 11]]]
      (is (= act exp)))))

(deftest beginning_of_line?_test1
  (testing "beginning of the line, 1"
    (let [node {:x 0, :y 2}
          act (beginning_of_line? node)
          exp true]
      (is (= act exp)))))

(deftest beginning_of_line?_test2
  (testing "beginning of the line, 2"
    (let [node {:x 1, :y 2}
          act (beginning_of_line? node)
          exp false]
      (is (= act exp)))))

(deftest end_of_line?_test1
  (testing "end of the line, 1"
    (let [node {:x 0, :y 2}
          plate {:width 3}
          act (end_of_line? node plate)
          exp false]
      (is (= act exp)))))

(deftest end_of_line?_test2
  (testing "end of the line, 2"
    (let [node {:x 2, :y 2}
          plate {:width 3}
          act (end_of_line? node plate)
          exp true]
      (is (= act exp)))))

(deftest top_line?_test1
  (testing "is top line, 1"
    (let [node {:x 0, :y 2}
          plate {:height 3}
          act (top_line? node plate)
          exp true]
      (is (= act exp)))))

(deftest top_line?_test2
  (testing "is top line, 2"
    (let [node {:x 0, :y 0}
          plate {:height 3}
          act (top_line? node plate)
          exp false]
      (is (= act exp)))))

(deftest bottom_line?_test1
  (testing "is bottom line, 1"
    (let [node {:x 0, :y 2}
          act (bottom_line? node)
          exp false]
      (is (= act exp)))))

(deftest bottom_line?_test2
  (testing "is bottom line, 2"
    (let [node {:x 1, :y 0}
          act (bottom_line? node)
          exp true]
      (is (= act exp)))))

(deftest left_node_test
  (testing "left node test"
    (let [node {:x 1, :y 0}
          act (left_node node)
          exp {:x 0, :y 0}]
      (is (= act exp)))))

(deftest right_node_test
  (testing "right node test"
    (let [node {:x 1, :y 2}
          act (right_node node)
          exp {:x 2, :y 2}]
      (is (= act exp)))))

(deftest upper_node_test
  (testing "upper node test"
    (let [node {:x 1, :y 2}
          act (upper_node node)
          exp {:x 1, :y 3}]
      (is (= act exp)))))

(deftest lower_node_test
  (testing "lower node test"
    (let [node {:x 1, :y 2}
          act (lower_node node)
          exp {:x 1, :y 1}]
      (is (= act exp)))))

(deftest first_after_second?_test1
  (testing "first after second node, 1"
    (let [node1 {:x 2, :y 1}
          node2 {:x 2, :y 1}
          act (first_after_second? node1 node2)
          exp false]
      (is (= act exp)))))

(deftest first_after_second?_test2
  (testing "first after second node, 2"
    (let [node1 {:x 3, :y 2}
          node2 {:x 2, :y 2}
          act (first_after_second? node1 node2)
          exp true]
      (is (= act exp)))))

(deftest get_upper_node_test1
  (testing "get upper node, 1"
    (let [node {:x 2, :y 1}
          plate {:data [[1 3 5] [2 4 6] [10 11 12]]
                 :width 3
                 :height 3}
          act (get_upper_node node plate)
          exp {:x 2, :y 2}]
      (is (= act exp)))))

(deftest get_upper_node_test2
  (testing "get upper node, 2"
    (let [node {:x 1, :y 2}
          plate {:data [[1 3 5] [2 4 6] [10 11 12]]
                 :width 3
                 :height 3}
          act (get_upper_node node plate)
          exp nil]
      (is (= act exp)))))

(deftest get_lower_node_test1
  (testing "get lower node, 1"
    (let [node {:x 0, :y 1}
          plate {:data [[1 3 5] [2 4 6] [10 11 12]]
                 :width 3
                 :height 3}
          act (get_lower_node node)
          exp {:x 0, :y 0}]
      (is (= act exp)))))

(deftest get_lower_node_test2
  (testing "get lower node, 2"
    (let [node {:x 1, :y 0}
          plate {:data [[1 3 5] [2 4 6] [10 11 12]]
                 :width 3
                 :height 3}
          act (get_lower_node node)
          exp nil]
      (is (= act exp)))))

(deftest get_right_node_test1
  (testing "get right node, 1"
    (let [node {:x 0, :y 1}
          plate {:data [[1 3 5] [2 4 6] [10 11 12]]
                 :width 3
                 :height 3}
          act (get_right_node node plate)
          exp {:x 1, :y 1}]
      (is (= act exp)))))

(deftest get_right_node_test2
  (testing "get right node, 2"
    (let [node {:x 2, :y 1}
          plate {:data [[1 3 5] [2 4 6] [10 11 12]]
                 :width 3
                 :height 3}
          act (get_right_node node plate)
          exp nil]
      (is (= act exp)))))


