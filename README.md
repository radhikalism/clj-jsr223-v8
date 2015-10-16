# clj-jsr223-v8

A Clojure wrapper around clj-v8, exposing a (probably, mostly) compliant JSR-223 `javax.script` API.

The `javax.script` API defines a generic interface for scripting engines, including ECMAScript engines for the JVM.

*clj-v8* provides a Java-based interface to (a specific and probably dated version of) V8, but this is not compliant with javax.scripting. Projects depending on *clj-v8* may want the option of loading pluggable script engines using javax.scripting, so *clj-jsr223-v8* aims to bridge that gap.


## Usage

Add `[clj-jsr223-v8 "0.1.0"]` to `:dependencies` in `project.clj`.

### Service API (recommended)

The `javax.script` API allows run-time discovery of registered engine services. *clj-jsr223-v8* registers its service unde the names `"v8"`, `"V8"`, and `"clj-v8"`.

Get a `V8ScriptEngineFactory` instance from *clj-jsr223-v8* using the JSR-223 API:

  (def my-manager (javax.script.ScriptingManager.))

  (def my-engine (.getEngineByName my-manager "v8")) ;; a V8ScriptEngine instance

  (.eval my-engine "123 + 456") ;; => "579"

### Direct API

To load the V8 engine directly (not recommended):

Import the script engine or the script engine factory classes as needed:

  (import '[clj-jsr223-v8 V8ScriptEngineFactory V8ScriptEngine])

Once imported, instantiate `V8ScriptEngineFactory` to get started.

  (def my-factory (V8ScriptEngineFactory.))

  (def my-engine (.getEngine my-factory)) ;; a V8ScriptEngine instance

  (.eval my-engine "123 + 456") ;; => "579"


## License

Copyright © 2015 Abhishek Reddy

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
