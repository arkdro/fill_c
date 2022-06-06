(ns graphics.bom
  (:gen-class)
  (:require [quil.core :as qc]
            [quil.middleware :as qmid]
            [clojure.core.async :as a :refer
             [>! <! >!! <!! go chan close! thread alts! alts!! timeout]]
            [clojure.tools.logging :as log]
            [clojure.pprint :refer :all])
  (:import java.lang.Math))

(def colors
  [
   [0xff 0xff 0xff]
   [0x00 0x00 0x00]
   [0xff 0x00 0x00]
   [0xff 0xff 0x00]
   [0x00 0xff 0x00]
   [0x00 0xff 0xff]
   [0x00 0x00 0xff]
   [0xff 0x00 0xff]
   ]
  )

(def key->color_num
  {
   :1 0
   :2 1
   :3 2
   :4 3
   :5 4
   :6 5
   :7 6
   :8 7

   :z 0
   :x 1
   :c 2
   :a 3
   :s 4
   :d 5
   :q 6
   :w 7

   :Z 0
   :X 1
   :C 2
   :A 3
   :S 4
   :D 5
   :Q 6
   :W 7
   }
  )

(def test_buffer
  [
             [7 0 1 2 5]
             [1 7 5 4 3]
             [6 1 1 6 3]
             ])
(def live-color [242 233 99])
(def dead-color [64 37 27])

(defn get_color
  [idx]
  (nth colors idx))

(def cell_base_coordinates
  "A shape like this:
   ^
  | |
   v
  The coordinates are [x y] increasing from left-top to right-bottom
  "
  [
   [1 0] ; top
   [2 1] ; right_up
   [2 2] ; right_down
   [1 3] ; bottom
   [0 2] ; left_down
   [0 1] ; left_up
   ]
  )

;; step between cells on X axis.
(def cell_step_x 2)

;; step between cells on Y axis. It is 2 (not 3) because rows are
;; compressed by 1 point on Y axis.
(def cell_step_y 2)

(def row_coordinate_offset
  "Even rows go without offset.
  Odd rows goes right."
  {:even [0 0]
   :odd [1 0]})

(defn add_row_offset
  [x y out_x out_y]
  (let [{[even_dx even_dy] :even
         [odd_dx odd_dy] :odd} row_coordinate_offset]
    (if (even? y)
      [(+ out_x even_dx) (+ out_y even_dy)]
      [(+ out_x odd_dx) (+ out_y odd_dy)])))

(defn calc_non_scaled_coordinates
  "Coordinates without scaling by cell graphic size (width and height)"
  [outer_x outer_y [in_cell_x in_cell_y]]
  (let [start_x (* outer_x cell_step_x)
        start_y (* outer_y cell_step_y)
        res_x (+ start_x in_cell_x)
        res_y (+ start_y in_cell_y)]
    (add_row_offset outer_x outer_y res_x res_y)))

(defn calc_scaled_coordinates
  "Coordinates in the graphic screen.
  They are scaled by the cell width and height."
  [width height [x y]]
  (let [res_x (* width x)
        res_y (* height y)]
    [res_x res_y]))

(defn cell_coordinates
  "Width, height - the sizes of one point."
  [width height x y]
  (let [non_scaled (map
                    #(calc_non_scaled_coordinates x y %)
                    cell_base_coordinates)
        scaled (map
                #(calc_scaled_coordinates width height %)
                non_scaled)]
    scaled))

(defn draw_cell
  [width height scale x y cell]
  (apply qc/fill (get_color cell))
  (qc/begin-shape)
  (dorun (map
          #(apply qc/vertex %)
          (cell_coordinates width height x y)))
  (qc/end-shape :close)
  ;; (qc/rect (* scale x) (* scale y) scale scale)
  )

(defn draw_row
  [width height scale y row]
  (dorun (map-indexed #(draw_cell width height scale %1 y %2) row)))

(defn draw-buffer
  [width height scale {buffer :buffer}]
  (dorun (map-indexed #(draw_row width height scale %1 %2) buffer)))

(defn get_chosen_color
  [event]
  (let [key (get event :key)]
    (get key->color_num key)))

(defn update_buffer_at_point
  [buffer event]
  (if-some [color (get_chosen_color event)]
    (assoc-in buffer [0 0] color)
    buffer))

(defn update_state_with_new_color
  [{:keys [buffer] :as state} event]
  (let [new_buffer (update_buffer_at_point buffer event)]
    (assoc state :buffer new_buffer)))

(defn key_typed
  [state event]
  (let [_ (log/info "key_typed, log, old state:" state ", event:" event)]
    (qc/redraw)
    (update_state_with_new_color state event)))

(defn increment_counter
  [state]
  (update state :i inc))

(defn update_state
  [state]
  (increment_counter state))

(defn setup
  "Setup the UI"
  [initial_board]
  (let [;; _ (qc/smooth) ;; Enable AA
        ;; _ (qc/frame-rate 10)
        state {:i 0
               :buffer test_buffer
               :board initial_board}]
    (qc/no-loop)
    state))

(defn run [{:keys [width height scale]} initial_board]
  (let [width (or width 10)
        height (or height 10)
        scale (or scale 50)]
    ;; Initialize the graphics
    (qc/defsketch fill
      :title "Fill"
      :setup (fn setup_fn [] (setup initial_board))
      ;; :no-bind-output is here to handle nil id for nREPL.
      ;; See quil issue #217 and cider issue #2138
      :features [:no-bind-output]
      :middleware [qmid/fun-mode]
      :key-typed key_typed
      :update update_state
      :draw (fn drawfn [old_state]
              (draw-buffer width height scale old_state))
      :size [(+ width width (* scale width))
             (+ height height (* scale height))])))

(def state
  (atom 0))

(defn state_runner
  [val]
  (let [new_val (mod (inc val) 8)]
    (reset! state val)
    (Thread/sleep 253)
    (recur new_val)))

(defn run_state_manager
  []
  (thread (state_runner 0)))

(defn -main [& args]
  (run_state_manager)
  (run {:width 20 :height 20 :scale 20}))
;; (graphics.bom/run {:width 40 :height 40 :scale 10})
