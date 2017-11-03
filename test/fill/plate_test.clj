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

