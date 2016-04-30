(ns clj-jsr223-v8.script-engine
  (:require v8.core)
  (:import [java.io Reader Writer]
           [javax.script ScriptException ScriptEngineFactory ScriptContext Bindings SimpleBindings]
           clj_jsr223_v8.V8ScriptEngineFactory))

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
    (v8.core/run-script-in-context v8-context script)
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
