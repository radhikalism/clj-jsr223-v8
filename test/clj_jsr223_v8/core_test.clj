(ns clj-jsr223-v8.core_test
  (:require [clojure.test :refer :all]
            [clojure.set]
            [clj-jsr223-v8 script-engine-factory script-engine])
  (:import [clj-jsr223-v8 V8ScriptEngineFactory V8ScriptEngine]))


(defn non-empty-string? [s]
  (and (string? s)
       (not (empty? s))))

(deftest engine-factory
  (testing "Engine factory"
    (testing "knows its engine"
      (is (= (.getParameter (V8ScriptEngineFactory.) javax.script.ScriptEngine/NAME)
             "clj-v8")))

    (testing "knows clj-v8's version"
      (is (non-empty-string? (.getParameter (V8ScriptEngineFactory.)
                                            javax.script.ScriptEngine/ENGINE_VERSION))))

    (testing "knows its supported extensions, including for *.js files"
      (is (some (partial = "js")
                (.getExtensions (V8ScriptEngineFactory.)))))
    
    (testing "knows its supported mime types, including application/javascript and text/javascript"
      (is (clojure.set/subset?
           #{"application/javascript" "text/javascript"}
           (set (.getMimeTypes (V8ScriptEngineFactory.))))))
    
    (testing "produces a compliant engine"
      (is (instance? javax.script.ScriptEngine
                     (.getScriptEngine (V8ScriptEngineFactory.)))))
  
    (testing "knows how to call a Java method from JS"
      (is (= (.getMethodCallSyntax (V8ScriptEngineFactory.)
                                   "java.lang.Math" "max" (into-array String ["1" "2"]))
             "java.lang.Math.max(1,2)")))))

(deftest engine
  (testing "Script engine"
    (testing "can eval correctly"
      (is (= (.eval (V8ScriptEngine.) "123")
             "123")))
    
    (testing "can eval readFile"
      (is (= (.eval (V8ScriptEngine.) "readFile('test/data/sample.js');")
             "123\n")))
    
    (testing  "can work in parallel with other script engines"
      (is (= (pmap #(.eval (V8ScriptEngine.) %) (repeat 20 "(function(){ return 5; })();"))
             (repeat 20 "5"))))
    
    (testing "can execute multiple scripts in the same context"
      (let [engine (V8ScriptEngine.)]
        (.eval engine "x = 17; y = {a: 6};")
        (is (= (.eval engine "x;")
               "17"))
        (is (= (.eval engine "y.a;")
               "6"))))))
