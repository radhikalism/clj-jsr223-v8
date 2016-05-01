(defproject clj-jsr223-v8 "0.1.7"
  :description "A JSR-223 (javax.script) API wrapping clj-v8"
  :url "https://github.com/arbscht"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :aot [clj-jsr223-v8.core]
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/data.json "0.2.6"]
                 [clj-v8 "0.1.5"]])
