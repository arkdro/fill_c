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

