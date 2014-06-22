(defproject adad "0.1.0"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2234"]
                 [prismatic/dommy "0.1.2"]]
  :plugins [[lein-cljsbuild "1.0.3"]]
  :cljsbuild
  {:builds
   [{:source-paths ["src/"]
     :compiler {:optimizations :whitespace
                :output-dir "out"
                :output-to "out/main.js"}}]})
