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

(deftest get_min_label_test
  (testing "get_min_label"
    (let [coord {:x 2, :y 1}
          width 4
          result_labels [[:no 2 :no 1] [:no 3 :no :no] [:no :no :no :no]]
          acc {:width width
               :result_labels result_labels}
          act (get_min_label coord acc)
          exp 1]
      (is (= act exp)))))

(deftest provisional_labels_equal_test
  (testing "provisional_labels_equal?"
    (let [cur 3
          mask 4
          act (provisional_labels_equal? cur mask)
          exp false]
      (is (= act exp)))))

(deftest representative_labels_equal_test1
  (testing "representative_labels_equal?, 1"
    (let [cur 3
          mask 4
          acc {:repr_tab [:no :no :no 2 2 :no :no :no :no :no :no :no]}
          act (representative_labels_equal? cur mask acc)
          exp true]
      (is (= act exp)))))

(deftest representative_labels_equal_test2
  (testing "representative_labels_equal?, 2"
    (let [cur 3
          mask 4
          acc {:repr_tab [:no :no :no 2 1 :no :no :no :no :no :no :no]}
          act (representative_labels_equal? cur mask acc)
          exp false]
      (is (= act exp)))))

(deftest last_label_test1
  (testing "last_label?, 1"
    (let [idx 2
          next_label [:no :no :last :no]
          act (last_label? idx next_label)
          exp true]
      (is (= act exp)))))

(deftest last_label_test2
  (testing "last_label?, 2"
    (let [idx 3
          next_label [:no :no :last :no]
          act (last_label? idx next_label)
          exp false]
      (is (= act exp)))))

(deftest update_repr_item_test
  (testing "update_repr_item"
    (let [u 3
          idx 2
          repr_tab [:no :no :no 2 1 :no]
          act (update_repr_item u idx repr_tab)
          exp [:no :no 3 2 1 :no]]
      (is (= act exp)))))

(deftest update_repr_tab_test
  (testing "update_repr_tab"
    (let [u 4
          idx 1
          acc {:repr_tab [:no :no :no 2 1 :no :no :no]
               :next_label [:no 3 :no 7 :last :no :no 4]}
          act (update_repr_tab u idx acc)
          exp {:repr_tab [:no 4 :no 4 4 :no :no 4]
               :next_label [:no 3 :no 7 :last :no :no 4]}]
      (is (= act exp)))))

(deftest update_next_label_test
  (testing "update_next_label"
    (let [u 1
          v 2
          acc {:tail [:no 7 4 2 1 :no :no :no]
               :next_label [:no 3 6 7 :last :no 4 :last]}
          act (update_next_label u v acc)
          exp {:tail [:no 7 4 2 1 :no :no :no]
               :next_label [:no 3 6 7 :last :no 4 2]}]
      (is (= act exp)))))

(deftest update_tail_test
  (testing "update_tail"
    (let [u 1
          v 2
          acc {:tail [:no 7 4 2 1 :no :no :no]}
          act (update_tail u v acc)
          exp {:tail [:no 4 4 2 1 :no :no :no]}]
      (is (= act exp)))))

(deftest merge_representative_labels_test
  (testing "merge_representative_labels"
    (let [cur_label 6
          mask_label 3
          acc {:repr_tab [:no 1 2 1 2 :no 2 1 :no :no :no :no]
               :next_label [:no 3 6 7 :last :no 4 :last :no :no :no :no]
               :tail [:no 7 4 :no :no :no :no :no :no :no :no :no]}
          act (merge_representative_labels cur_label mask_label acc)
          exp {:repr_tab [:no 1 1 1 1 :no 1 1 :no :no :no :no]
               :next_label [:no 3 6 7 :last :no 4 2 :no :no :no :no]
               :tail [:no 4 4 :no :no :no :no :no :no :no :no :no]}]
      (is (= act exp)))))

