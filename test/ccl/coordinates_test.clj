(ns ccl.ccl_test
  (:require [clojure.test :refer :all]
            [ccl.coordinates :refer :all]))

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

(deftest get_mask_coordinates_8_test1
  (testing "get mask coordinates, 1"
    (let [coord {:x 0, :y 0}
          width 4
          act (get_mask_coordinates_8 coord width)
          exp []]
      (is (= act exp)))))

(deftest get_mask_coordinates_8_test2
  (testing "get mask coordinates, 2"
    (let [coord {:x 0, :y 1}
          width 4
          act (get_mask_coordinates_8 coord width)
          exp [{:x 0, :y 0} {:x 1, :y 0}]]
      (is (= act exp)))))

(deftest get_mask_coordinates_8_test3
  (testing "get mask coordinates, 3"
    (let [coord {:x 1, :y 1}
          width 4
          act (get_mask_coordinates_8 coord width)
          exp [{:x 0, :y 0} {:x 1, :y 0} {:x 2, :y 0} {:x 0, :y 1}]]
      (is (= act exp)))))

(deftest get_mask_coordinates_8_test3
  (testing "get mask coordinates, 3"
    (let [coord {:x 3, :y 0}
          width 4
          act (get_mask_coordinates_8 coord width)
          exp [{:x 2, :y 0}]]
      (is (= act exp)))))

(deftest get_mask_coordinates_8_test4
  (testing "get mask coordinates, 4"
    (let [coord {:x 3, :y 1}
          width 4
          act (get_mask_coordinates_8 coord width)
          exp [{:x 2, :y 0} {:x 3, :y 0} {:x 2, :y 1}]]
      (is (= act exp)))))

(deftest get_mask_coordinates_6_test1
  (testing "get mask coordinates, 6 connectivity, 1"
    (let [coord {:x 0, :y 0}
          width 4
          act (get_mask_coordinates_6 coord width)
          exp []]
      (is (= act exp)))))

(deftest get_mask_coordinates_6_test2
  (testing "get mask coordinates, 6 connectivity, 2"
    (let [coord {:x 0, :y 1}
          width 4
          act (get_mask_coordinates_6 coord width)
          exp [{:x 0, :y 0}]]
      (is (= act exp)))))

(deftest get_mask_coordinates_6_test3
  (testing "get mask coordinates, 6 connectivity, 3"
    (let [coord {:x 1, :y 1}
          width 4
          act (get_mask_coordinates_6 coord width)
          exp [{:x 0, :y 0} {:x 1, :y 0} {:x 0, :y 1}]]
      (is (= act exp)))))

(deftest get_mask_coordinates_6_test4
  (testing "get mask coordinates, 6 connectivity, 4"
    (let [coord {:x 3, :y 1}
          width 4
          act (get_mask_coordinates_6 coord width)
          exp [{:x 2, :y 0} {:x 3, :y 0} {:x 2, :y 1}]]
      (is (= act exp)))))

(deftest get_mask_coordinates_6_test5
  (testing "get mask coordinates, 6 connectivity, 5"
    (let [coord {:x 3, :y 2}
          width 4
          act (get_mask_coordinates_6 coord width)
          exp [{:x 3, :y 1} {:x 2, :y 2}]]
      (is (= act exp)))))

