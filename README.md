# supabase-clj


[![Clojars Project](https://img.shields.io/clojars/v/co.cljazz/supabase-clj.svg)](https://clojars.org/co.cljazz/supabase-clj)

Supabase client with support **clj/cljs**

## Discalmer

> This is a version we are using in an internal code, so
> it my change some contract or way of use it
>
> We have gotrue for now

## Install


```edn
co.cljazz/supabase-clj {:mvn/version "0.0.1"}
```

## Issues 

https://github.com/cljazz/supabase-clj/issues


## References

https://github.com/supabase/gotrue

https://github.com/supabase/gotrue-js

## Build

To generate the `.jar` we have the alias `uberjar`:

```
clojure -M:uberjar
```

### Deploy

We use **clojars** to host our `.jar`.

To deploy to clojars it is necessary to generate the "pom" and export the environment variables with clojars *login/password* (tokens), then run the alias `deploy-clojars`:

```
clojure -X:deps mvn-pom
env CLOJARS_USERNAME=username CLOJARS_PASSWORD=clojars-token
clojure -X:deploy-clojars
```
