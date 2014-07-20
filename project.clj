(defproject adad "0.1.0"
  :clojurescript? true
  :description "ClojureScript implementation of game 2048"
  :url "https://github.com/flash42/adad"
  :scm {:url "git@github.com:flash42/adad.git"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2234"]
                 [prismatic/dommy "0.1.2"]
                 [com.cemerick/clojurescript.test "0.3.1"]]
  :hooks [leiningen.cljsbuild]
  :plugins [[lein-cljsbuild "1.0.3"]
            [com.cemerick/clojurescript.test "0.3.1"]]
  :cljsbuild
  {:builds
   {:test {:source-paths ["src" "test"]
           :incremental? true
           :compiler {:output-to "out/main.js"
                      :optimizations :whitespace
                      :pretty-print true}}}
      :test-commands {"unit" ["phantomjs" :runner
                           "out/main.js"]}})
