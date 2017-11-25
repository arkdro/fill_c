(defproject fill "0.0.2-SNAPSHOT"
  :description "Flood fill"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-beta2"]
                 [org.clojure/tools.cli "0.3.5"]
                 [cheshire "5.8.0"]]
  :main ^:skip-aot fill.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
