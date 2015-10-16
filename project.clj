(defproject clj-jsr223-v8 "0.1.0-SNAPSHOT"
  :description "A JSR-223 (javax.script) API wrapping clj-v8"
  :url "https://github.com/arbscht"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :aot [clj-jsr223-v8.core]
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [clj-v8 "0.1.5"]]
  :release-tasks [["vcs" "assert-committed"]
                  ["change" "version" "leiningen.release/bump-version" "release"]
                  ["vcs" "commit"]
                  ["deploy" "clojars"]
                  ["change" "version" "leiningen.release/bump-version"]
                  ["vcs" "commit"]])
