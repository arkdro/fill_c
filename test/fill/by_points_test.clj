(ns fill.by_points_test
  (:require [clojure.test :refer :all]
            [fill.by_points :refer :all]))

(deftest find_begin_of_cut_test
  (testing "find begin of cut"
    (let [node {:x 1, :y 1}
          target_color 4
          plate {:data [[1 3 5 1 2] [4 4 4 2 6] [1 2 2 1 1]]}
          act (find_begin_of_cut node target_color plate)
          exp {:x 0, :y 1}]
      (is (= act exp)))))

(deftest find_end_of_cut_test
  (testing "find end of cut"
    (let [node {:x 1, :y 1}
          target_color 4
          plate {:data [[1 3 5 1 2] [4 4 4 2 6] [1 2 2 1 1]]
                 :width 5}
          act (find_end_of_cut node target_color plate)
          exp {:x 2, :y 1}]
      (is (= act exp)))))

