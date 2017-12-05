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

