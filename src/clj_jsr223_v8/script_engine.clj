(ns clj-jsr223-v8.script-engine
  (:require v8.core
            [clojure.data.json :as json]
            [clojure.string :as string])
  (:import [java.io Reader Writer]
           [javax.script ScriptException ScriptEngineFactory ScriptContext Bindings SimpleBindings]
           clj_jsr223_v8.V8ScriptEngineFactory))

(def js-escape
  "Characters to escape in a given string of ECMAScript
   source code, such that ES eval() can accept it. Conforms
   to the replacement map argument of clojure.string/escape."
  {\' "\\'"
   \" "\\\""
   \\ "\\\\"
   \newline "\\n"
   \return "\\r"
   \u2028 "\\u2028"
   \u2029 "\\u2029"})

(defn json-wrap-script
  "Returns a string representing an ECMAScript program that
   would return a JSON value of the result of escaping and evaluating
   script as ES code. Note: undefined may be returned as a literal."
  [script]
  (str "JSON.stringify(eval('" (string/escape script js-escape) "'));"))

(defn read-json-result
  "Returns an object representing the stringified JSON value. Note:
   handles undefined as null."
  [stringified]
  (json/read-str (if (= stringified "undefined") "null" stringified)))

(defn -init
  []
  [[] (ref {:v8-context (v8.core/create-context)})])

(defn -finalize
  [this]
  (try
    (.cleanup this)
    (finally
     (.finalizeSuper this))))

(defn ^Bindings -createBindings
  [this]
  (SimpleBindings.))

(defn -eval-String-ScriptContext
  [this, ^String script, ^ScriptContext context]
  (if-let [v8-context (:v8-context @(.state this))]
    (->> (json-wrap-script script)
         (v8.core/run-script-in-context v8-context)
         (read-json-result))
    (throw (NullPointerException. "v8 context already cleaned up."))))

(defn -eval-Reader-ScriptContext
  [this, ^Reader reader, ^ScriptContext context]
  (.eval this (slurp reader) context))

(defn ^ScriptEngineFactory -getFactory
  [this]
  (V8ScriptEngineFactory.))

(defn -cleanup
  [this]
  (when-let [v8-context (:v8-context @(.state this))]
    (dosync (alter (.state this) assoc :v8-context nil))
    (v8.core/cleanup-context v8-context)))
