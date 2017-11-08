(ns fill.by_points_test
  (:require [clojure.test :refer :all]
            [fill.by_points :refer :all]))

(deftest find_begin_of_cut_test1
  (testing "find begin of cut, 1"
    (let [node {:x 1, :y 1}
          old_color 4
          plate {:data [[1 3 5 1 2] [4 4 4 2 6] [1 2 2 1 1]]}
          act (find_begin_of_cut node old_color plate)
          exp {:x 0, :y 1}]
      (is (= act exp)))))

(deftest find_begin_of_cut_test2
  (testing "find begin of cut, 2"
    (let [node {:x 1, :y 0}
          old_color 3
          plate {:data [[1 3 3 1 2]]
                 :width 5}
          act (find_begin_of_cut node old_color plate)
          exp {:x 1, :y 0}]
      (is (= act exp)))))

(deftest find_begin_of_cut_test3
  (testing "find begin of cut, 3"
    (let [node {:x 2, :y 0}
          old_color 3
          plate {:data [[1 3 3 1 2]]
                 :width 5}
          act (find_begin_of_cut node old_color plate)
          exp {:x 1, :y 0}]
      (is (= act exp)))))

(deftest find_end_of_cut_test1
  (testing "find end of cut, 1"
    (let [node {:x 1, :y 1}
          old_color 4
          plate {:data [[1 3 5 1 2] [4 4 4 2 6] [1 2 2 1 1]]
                 :width 5}
          act (find_end_of_cut node old_color plate)
          exp {:x 2, :y 1}]
      (is (= act exp)))))

(deftest find_end_of_cut_test2
  (testing "find end of cut, 2"
    (let [node {:x 3, :y 1}
          old_color 2
          plate {:data [[1 3 5 1 2] [4 4 4 2 6]]
                 :width 5}
          act (find_end_of_cut node old_color plate)
          exp {:x 3, :y 1}]
      (is (= act exp)))))

(deftest find_end_of_cut_test3
  (testing "find end of cut, 3"
    (let [node {:x 4, :y 1}
          old_color 6
          plate {:data [[1 3 5 1 2] [4 4 4 2 6]]
                 :width 5}
          act (find_end_of_cut node old_color plate)
          exp {:x 4, :y 1}]
      (is (= act exp)))))

(deftest cut_done?_test1
  (testing "cut done, 1"
    (let [node1 {:x 5, :y 1}
          node2 {:x 5, :y 1}
          act (cut_done? node1 node2)
          exp false]
      (is (= act exp)))))

(deftest cut_done?_test2
  (testing "cut done, 2"
    (let [node1 {:x 6, :y 1}
          node2 {:x 5, :y 1}
          act (cut_done? node1 node2)
          exp true]
      (is (= act exp)))))

(deftest add_upper_node_test1
  (testing "add upper node, 1"
    (let [node {:x 2, :y 1}
          queue (clojure.lang.PersistentQueue/EMPTY)
          old_color 4
          plate {:data [[1 3 5 1 2] [4 4 4 2 6] [5 3 4 1 1]]
                 :width 5
                 :height 3}
          new_queue (add_upper_node node queue old_color plate)
          item (peek new_queue)
          rest (pop new_queue)
          act [item rest]
          exp [{:x 2, :y 2} ()]]
      (is (= act exp)))))

(deftest add_upper_node_test2
  (testing "add upper node, 2"
    (let [node {:x 1, :y 2}
          queue (clojure.lang.PersistentQueue/EMPTY)
          old_color 3
          plate {:data [[1 3 5 1 2] [4 4 4 2 6] [5 3 4 1 1]]
                 :width 5
                 :height 3}
          new_queue (add_upper_node node queue old_color plate)
          item (peek new_queue)
          rest (pop new_queue)
          act [item rest]
          exp [nil ()]]
      (is (= act exp)))))

(deftest add_lower_node_test1
  (testing "add lower node, 1"
    (let [node {:x 4, :y 1}
          queue (clojure.lang.PersistentQueue/EMPTY)
          old_color 6
          plate {:data [[1 3 5 1 6] [4 4 4 2 6] [5 3 4 1 1]]
                 :width 5
                 :height 3}
          new_queue (add_lower_node node queue old_color plate)
          item (peek new_queue)
          rest (pop new_queue)
          act [item rest]
          exp [{:x 4, :y 0} ()]]
      (is (= act exp)))))

(deftest add_lower_node_test2
  (testing "add lower node, 2"
    (let [node {:x 4, :y 0}
          queue (clojure.lang.PersistentQueue/EMPTY)
          old_color 6
          plate {:data [[1 3 5 1 6] [4 4 4 2 6] [5 3 4 1 1]]
                 :width 5
                 :height 3}
          new_queue (add_lower_node node queue old_color plate)
          item (peek new_queue)
          rest (pop new_queue)
          act [item rest]
          exp [nil ()]]
      (is (= act exp)))))

(deftest iterate_cut_test
  (testing "iterate cut"
    (let [begin {:x 0, :y 1}
          end {:x 2, :y 1}
          queue (clojure.lang.PersistentQueue/EMPTY)
          old_color 4
          new_color 6
          plate {:data [[4 3 5 1 6] [4 4 4 2 6] [5 3 4 1 1]]
                 :width 5
                 :height 3}
          [new_queue act_plate] (iterate_cut begin end queue
                                             old_color new_color
                                             plate)
          act_seq (seq new_queue)
          exp_seq [{:x 0, :y 0} {:x 2, :y 2}]
          exp_plate {:data [[4 3 5 1 6] [6 6 6 2 6] [5 3 4 1 1]]
                     :width 5
                     :height 3}]
      (is (= act_seq exp_seq))
      (is (= act_plate exp_plate)))))

(deftest process_one_node_test
  (testing "process one node"
    (let [node {:x 2, :y 1}
          empty_queue (clojure.lang.PersistentQueue/EMPTY)
          queue (conj empty_queue node)
          old_color 4
          new_color 6
          plate {:data [[4 3 5 1 6] [4 4 4 2 6] [5 3 4 1 1]]
                 :width 5
                 :height 3}
          [new_queue act_plate] (process_one_node queue
                                                  old_color
                                                  new_color
                                                  plate)
          act_seq (seq new_queue)
          exp_seq [{:x 0, :y 0} {:x 2, :y 2}]
          exp_plate {:data [[4 3 5 1 6] [6 6 6 2 6] [5 3 4 1 1]]
                     :width 5
                     :height 3}]
      (is (= act_seq exp_seq))
      (is (= act_plate exp_plate)))))

(deftest step_test
  (testing "step"
    (let [node {:x 2, :y 1}
          empty_queue (clojure.lang.PersistentQueue/EMPTY)
          queue (conj empty_queue node)
          old_color 4
          new_color 6
          plate {:data [[4 3 5 1 6] [4 4 4 2 6] [5 3 4 1 1]]
                 :width 5
                 :height 3}
          act (step queue old_color new_color plate)
          exp {:data [[6 3 5 1 6] [6 6 6 2 6] [5 3 6 1 1]]
               :width 5
               :height 3}]
      (is (= act exp)))))

(deftest fill_test
  (testing "fill"
    (let [node {:x 2, :y 1}
          new_color 6
          plate {:data [[4 3 5 1 6] [4 4 4 2 6] [5 3 4 1 1]]
                 :width 5
                 :height 3}
          act (fill node new_color plate)
          exp {:data [[6 3 5 1 6] [6 6 6 2 6] [5 3 6 1 1]]
               :width 5
               :height 3}]
      (is (= act exp)))))

