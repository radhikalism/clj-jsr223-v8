(ns clj-jsr223-v8.script-engine-factory
  (:import [java.io Reader Writer]
           [javax.script ScriptException ScriptEngine]
           clj-jsr223-v8.V8ScriptEngine))

(defn -getEngineName
  [this]
  (first (.getNames this)))

(defn -getEngineVersion
  [this]
  "0.1.5")

(defn -getExtensions
  [this]
  ["js"])

(defn -getLanguageName
  [this]
  "ECMAScript")

(defn -getLanguageVersion
  [this]
  "5")

(defn -getMethodCallSyntax
  [this obj m args]
  ;; No "real" varargs available; they should be given as an explicit array.
  ;; Example use: (.getMethodCallSyntax factory "java.lang.Math" (into-array String ["1" "2"]))
  ;;              => "java.lang.Math.max(1,2)"
  (str obj "." m "("
       (clojure.string/join "," args)
       ")"))

(defn -getMimeTypes
  [this]
  ["application/javascript", "text/javascript"])

(defn -getNames
  [this]
  ["clj-v8", "v8", "V8"])

(defn ^String -getOutputStatement
  [this ^String to-display]
  (str "console.log(" to-display ");"))

(defn -getParameter
  [this ^String key]
  (cond
   (= ScriptEngine/ENGINE key)
   (.getEngineName this)

   (= ScriptEngine/ENGINE_VERSION key)
   (.getEngineVersion this)

   (= ScriptEngine/NAME key)
   (first (.getNames this))

   (= ScriptEngine/LANGUAGE key)
   (.getLanguageName this)

   (= ScriptEngine/LANGUAGE_VERSION key)
   (.getLanguageVersion this)

   :else nil))

(defn ^ScriptEngine -getScriptEngine
  [this]
  (V8ScriptEngine.))
