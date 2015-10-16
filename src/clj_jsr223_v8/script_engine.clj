(ns clj-jsr223-v8.script-engine
  (:require v8.core)
  (:import [java.io Reader Writer]
           [javax.script ScriptException ScriptEngineFactory ScriptContext Bindings SimpleBindings]
           clj_jsr223_v8.V8ScriptEngineFactory))

(defn ^Bindings -createBindings
  [this]
  (SimpleBindings.))

(defn -eval-String-ScriptContext
  [this, ^String script, ^ScriptContext context]
  (if-let [v8-context (.getAttribute context "V8_CONTEXT")]
    (v8.core/run-script-in-context v8-context script)
    (let [new-v8-context (v8.core/create-context)]
      (.put this "V8_CONTEXT" new-v8-context)
      (v8.core/run-script-in-context new-v8-context script))))

(defn -eval-Reader-ScriptContext
  [this, ^Reader reader, ^ScriptContext context]
  (.eval this (slurp reader) context))

(defn ^ScriptEngineFactory -getFactory
  [this]
  (V8ScriptEngineFactory.))
