(set-env!
  :source-paths #{"src" "test"}
  :dependencies '[[adzerk/boot-test "1.2.0"]
                  [aleph "0.4.1"]
                  [com.stuartsierra/component "0.3.2"]
                  [org.clojure/clojure "1.9.0-alpha14"]
                  [org.clojure/core.async "0.3.441"]
                  [org.clojure/core.match "0.3.0-alpha4"]])

(require '[adzerk.boot-test :refer [test]])
