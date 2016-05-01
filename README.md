# clj-jsr223-v8

This is a Clojure wrapper around [clj-v8](https://github.com/circleci/clj-v8/), exposing a (probably, mostly) compliant [JSR-223](https://docs.oracle.com/javase/8/docs/technotes/guides/scripting/prog_guide/api.html) `javax.script` API.

The `javax.script` API defines a generic interface for scripting engines, including ECMAScript engines for the JVM.

*clj-v8* provides a Java-based interface to (a specific and probably dated version of) V8, but this is not compliant with javax.scripting. Projects depending on *clj-v8* may want the option of loading pluggable script engines using javax.scripting, so *clj-jsr223-v8* aims to bridge that gap.


## Usage

Add `[clj-jsr223-v8 "0.1.7"]` to `:dependencies` in `project.clj`.

### Service API (recommended)

The `javax.script` API allows run-time discovery of registered engine services. *clj-jsr223-v8* registers its service under the names `"clj-v8"`, `"v8"`, and `"V8"` (in that order of preference).

Get a `V8ScriptEngine` instance from *clj-jsr223-v8* using the JSR-223 API:

```clojure
(def engines (javax.script.ScriptingManager.))

(def v8-engine (.getEngineByName engines "clj-v8")) ;; A V8ScriptEngine instance
                                                    ;; (You might use "nashorn" on JDK8 here
                                                    ;;  for a different engine.)

(.eval v8-engine "123 + 456") ;; => 579

;; Context persists in each engine instance...

(.eval v8-engine "var foo = 1 + 1;") ;; => nil

(.eval v8-engine "foo") ;; => 2
```

Like in Nashorn, values returned by `(.eval ...)` are Clojure/Java objects (though not guaranteed to be exactly the same at this time!). These are constructed from a JSON-stringified marshalled representation of the underlying V8 value. This means e.g. `Object` instances are represented as maps:

```clojure
(.eval v8-engine "var o = new Object(); o.foo = 1; o.bar = 2; o;") ;; => {"foo" 1, "bar" 2}
```

`undefined` and `null` ECMAScript values each appear as `nil`.

#### Cleaning up

`V8ScriptEngine` instances implement a `cleanup` method in addition to `ScriptEngine` extensions. This can be used to free the resources held by the underlying native V8 engine attached to the instance. Note: cleaning up is final and the instance will be unusable afterwards.

`V8ScriptEngine` also triggers `cleanup` automatically on `finalize` (i.e. just before being garbage-collected), so it is not required to manually clean up resources.


### Direct API

To load the V8 engine directly (not recommended):

Import the script engine or the script engine factory classes as needed:

```clojure
(import '[clj_jsr223_v8 V8ScriptEngineFactory V8ScriptEngine]) ;; Note the underscores!
```

Once imported, instantiate `V8ScriptEngineFactory` to get started.

```clojure
(def my-factory (V8ScriptEngineFactory.))

(def my-engine (.getEngine my-factory)) ;; a V8ScriptEngine instance

(.eval my-engine "123 + 456") ;; => "579"
```

## License

Copyright © 2015-2016 Abhishek Reddy

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
