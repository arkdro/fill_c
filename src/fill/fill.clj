(ns fill.by_points)

(defn step
  "One step of filling"
  [queue
   target_color
   new_color
   {:keys [width height data] :as plate}]
  (assert false "not implemented"))

(defn fill
  "Flood fill"
  [node new_color plate]
  (let [target_color (get_color node plate)]
    (cond
      (same_colors? target_color new_color) plate
      (not_same_colors? (get_color node plate) target_color) plate
      :default (let [queue (conj (clojure.lang.PersistentQueue/EMPTY) node)]
                 (step queue target_color new_color plate)))))

