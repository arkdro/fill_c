(ns ccl.ccl_test
  (:require [clojure.test :refer :all]
            [ccl.ccl :refer :all]))

(deftest init_tables_test
  (testing "init tables"
    (let [width 4
          height 3
          act (init_tables width height)
          exp {:repr_tab [:no :no :no :no :no :no :no :no :no :no :no :no]
               :next_label [:no :no :no :no :no :no :no :no :no :no :no :no]
               :result_labels [[:no :no :no :no]
                               [:no :no :no :no]
                               [:no :no :no :no]]
               :tail [:no :no :no :no :no :no :no :no :no :no :no :no]}]
      (is (= act exp)))))

(deftest get_color_test
  (testing "get color"
    (let [point {:x 1, :y 2}
          data [[0 1 2 1] [3 4 5 13] [6 7 8 9]]
          act (get_color point data)
          exp 7]
      (is (= act exp)))))

(deftest foreground_point_test
  (testing "foreground point?"
    (let [coord {:x 1, :y 2}
          color 6
          data [[0 1 2 1] [3 4 5 13] [6 7 8 9]]
          act (foreground_point? coord color data)
          exp false]
      (is (= act exp)))))

(deftest get_left_coord_test1
  (testing "get left coordinates, 1"
    (let [coord {:x 1, :y 2}
          act (get_left_coord coord)
          exp {:x 0, :y 2}]
      (is (= act exp)))))

(deftest get_left_coord_test2
  (testing "get left coordinates, 2"
    (let [coord {:x 0, :y 2}
          act (get_left_coord coord)
          exp nil]
      (is (= act exp)))))

(deftest get_upper_coord_test1
  (testing "get upper coordinates, 1"
    (let [coord {:x 1, :y 2}
          act (get_upper_coord coord)
          exp {:x 1, :y 1}]
      (is (= act exp)))))

(deftest get_upper_coord_test2
  (testing "get upper coordinates, 2"
    (let [coord {:x 1, :y 0}
          act (get_upper_coord coord)
          exp nil]
      (is (= act exp)))))

(deftest get_upper_left_coord_test1
  (testing "get upper-left coordinates, 1"
    (let [coord {:x 1, :y 2}
          act (get_upper_left_coord coord)
          exp {:x 0, :y 1}]
      (is (= act exp)))))

(deftest get_upper_left_coord_test2
  (testing "get upper-left coordinates, 2"
    (let [coord {:x 0, :y 2}
          act (get_upper_left_coord coord)
          exp nil]
      (is (= act exp)))))

(deftest get_upper_left_coord_test3
  (testing "get upper-left coordinates, 3"
    (let [coord {:x 1, :y 0}
          act (get_upper_left_coord coord)
          exp nil]
      (is (= act exp)))))

(deftest get_upper_right_coord_test1
  (testing "get upper-right coordinates, 1"
    (let [coord {:x 1, :y 2}
          width 4
          act (get_upper_right_coord coord width)
          exp {:x 2, :y 1}]
      (is (= act exp)))))

(deftest get_upper_right_coord_test2
  (testing "get upper-right coordinates, 2"
    (let [coord {:x 1, :y 0}
          width 4
          act (get_upper_right_coord coord width)
          exp nil]
      (is (= act exp)))))

(deftest get_upper_right_coord_test3
  (testing "get upper-right coordinates, 3"
    (let [coord {:x 3, :y 2}
          width 4
          act (get_upper_right_coord coord width)
          exp nil]
      (is (= act exp)))))

(deftest get_mask_coordinates_test1
  (testing "get mask coordinates, 1"
    (let [coord {:x 0, :y 0}
          width 4
          act (get_mask_coordinates coord width)
          exp []]
      (is (= act exp)))))

(deftest get_mask_coordinates_test2
  (testing "get mask coordinates, 2"
    (let [coord {:x 0, :y 1}
          width 4
          act (get_mask_coordinates coord width)
          exp [{:x 0, :y 0} {:x 1, :y 0}]]
      (is (= act exp)))))

(deftest get_mask_coordinates_test3
  (testing "get mask coordinates, 3"
    (let [coord {:x 1, :y 1}
          width 4
          act (get_mask_coordinates coord width)
          exp [{:x 0, :y 0} {:x 1, :y 0} {:x 2, :y 0} {:x 0, :y 1}]]
      (is (= act exp)))))

(deftest get_mask_coordinates_test3
  (testing "get mask coordinates, 3"
    (let [coord {:x 3, :y 0}
          width 4
          act (get_mask_coordinates coord width)
          exp [{:x 2, :y 0}]]
      (is (= act exp)))))

(deftest get_mask_coordinates_test4
  (testing "get mask coordinates, 4"
    (let [coord {:x 3, :y 1}
          width 4
          act (get_mask_coordinates coord width)
          exp [{:x 2, :y 0} {:x 3, :y 0} {:x 2, :y 1}]]
      (is (= act exp)))))

(deftest get_mask_color_test
  (testing "get mask color"
    (let [coord {:x 3, :y 1}
          width 4
          data [[0 1 2 1] [3 4 5 13] [6 7 8 9]]
          act (get_mask_colors coord width data)
          exp [2 1 5]]
      (is (= act exp)))))

(deftest background_mask_test1
  (testing "background_mask?, 1"
    (let [coord {:x 2, :y 1}
          width 4
          color 2
          data [[0 1 2 1] [3 4 5 13] [6 7 8 9]]
          act (background_mask? coord width color data)
          exp false]
      (is (= act exp)))))

(deftest background_mask_test2
  (testing "background_mask?, 2"
    (let [coord {:x 2, :y 1}
          width 4
          color 3
          data [[0 1 2 1] [3 4 5 13] [6 7 8 9]]
          act (background_mask? coord width color data)
          exp true]
      (is (= act exp)))))

(deftest assign_new_label_test
  (testing "assign_new_label"
    (let [coord {:x 2, :y 1}
          acc {:repr_tab [:no :no :no :no :no :no :no :no :no :no :no :no]
               :next_label [:no :no :no :no :no :no :no :no :no :no :no :no]
               :result_labels [[:no :no :no :no]
                               [:no :no :no :no]
                               [:no :no :no :no]]
               :tail [:no :no :no :no :no :no :no :no :no :no :no :no]
               :m 1}
          act (assign_new_label acc coord)
          exp {:repr_tab [:no 1 :no :no :no :no :no :no :no :no :no :no]
               :next_label [:no :last :no :no :no :no :no :no :no :no :no :no]
               :result_labels [[:no :no :no :no]
                               [:no :no   1 :no]
                               [:no :no :no :no]]
               :tail [:no 1 :no :no :no :no :no :no :no :no :no :no]
               :m 2}]
      (is (= act exp)))))

(deftest get_label_test
  (testing "get_label"
    (let [coord {:x 2, :y 1}
          data [[0 1 2 1] [3 4 5 13] [6 7 8 9]]
          act (get_label coord data)
          exp 5]
      (is (= act exp)))))

(deftest get_mask_labels_test1
  (testing "get_mask_labels, 1"
    (let [coord {:x 0, :y 0}
          width 4
          result_labels [[:no 2 :no 1] [:no 2 :no :no] [:no :no :no :no]]
          act (get_mask_labels coord width result_labels)
          exp []]
      (is (= act exp)))))

(deftest get_mask_labels_test2
  (testing "get_mask_labels, 2"
    (let [coord {:x 0, :y 1}
          width 4
          result_labels [[:no 2 :no 1] [:no 2 :no :no] [:no :no :no :no]]
          act (get_mask_labels coord width result_labels)
          exp [2]]
      (is (= act exp)))))

(deftest get_mask_labels_test3
  (testing "get_mask_labels, 3"
    (let [coord {:x 2, :y 1}
          width 4
          result_labels [[:no 2 :no 1] [:no 2 :no :no] [:no :no :no :no]]
          act (get_mask_labels coord width result_labels)
          exp [2 1 2]]
      (is (= act exp)))))

