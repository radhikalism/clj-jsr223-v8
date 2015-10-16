(ns clj-jsr223-v8.core)

(gen-class
 :name clj_jsr223_v8.V8ScriptEngine
 :extends javax.script.AbstractScriptEngine
 :impl-ns clj-jsr223-v8.script-engine
 :main false)

(gen-class
 :name clj_jsr223_v8.V8ScriptEngineFactory
 :implements [javax.script.ScriptEngineFactory]
 :impl-ns clj-jsr223-v8.script-engine-factory
 :main false)
