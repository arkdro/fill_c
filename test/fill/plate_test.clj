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

