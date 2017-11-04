(ns fill.by_points_test
  (:require [clojure.test :refer :all]
            [fill.by_points :refer :all]))

(deftest find_begin_of_cut_test1
  (testing "find begin of cut, 1"
    (let [node {:x 1, :y 1}
          target_color 4
          plate {:data [[1 3 5 1 2] [4 4 4 2 6] [1 2 2 1 1]]}
          act (find_begin_of_cut node target_color plate)
          exp {:x 0, :y 1}]
      (is (= act exp)))))

(deftest find_begin_of_cut_test2
  (testing "find begin of cut, 2"
    (let [node {:x 1, :y 0}
          target_color 3
          plate {:data [[1 3 3 1 2]]
                 :width 5}
          act (find_begin_of_cut node target_color plate)
          exp {:x 1, :y 0}]
      (is (= act exp)))))

(deftest find_begin_of_cut_test3
  (testing "find begin of cut, 3"
    (let [node {:x 2, :y 0}
          target_color 3
          plate {:data [[1 3 3 1 2]]
                 :width 5}
          act (find_begin_of_cut node target_color plate)
          exp {:x 1, :y 0}]
      (is (= act exp)))))

(deftest find_end_of_cut_test1
  (testing "find end of cut, 1"
    (let [node {:x 1, :y 1}
          target_color 4
          plate {:data [[1 3 5 1 2] [4 4 4 2 6] [1 2 2 1 1]]
                 :width 5}
          act (find_end_of_cut node target_color plate)
          exp {:x 2, :y 1}]
      (is (= act exp)))))

(deftest find_end_of_cut_test2
  (testing "find end of cut, 2"
    (let [node {:x 3, :y 1}
          target_color 2
          plate {:data [[1 3 5 1 2] [4 4 4 2 6]]
                 :width 5}
          act (find_end_of_cut node target_color plate)
          exp {:x 3, :y 1}]
      (is (= act exp)))))

