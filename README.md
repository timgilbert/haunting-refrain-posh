# haunting-refrain

Experiment with [datascript](https://github.com/tonsky/datascript) + 
[posh](https://github.com/mpdairy/posh) + [re-frame](https://github.com/Day8/re-frame)

[![CircleCI](https://circleci.com/gh/timgilbert/haunting-refrain-posh/tree/develop.svg?style=svg)](https://circleci.com/gh/timgilbert/haunting-refrain-posh/tree/develop)

## Development Mode

Basically, `lein repl`, `(fig)`, `(cljs)`. I generally connect to the nrepl
from IntelliJ, so the sequence looks roughly like this:

```
lein repl
Loading sidecar...
nREPL server started on port 4444 on host 0.0.0.0 - nrepl://0.0.0.0:4444
[...]
user=> 
```

Connect an IntelliJ remote nREPL to localhost:4444, or just continue with the terminal.

```
user=> (fig)
Figwheel: Starting server at http://0.0.0.0:3449
Figwheel: Watching build - dev
Compiling "resources/public/js/compiled/app.js" from ["src/cljs"]...
()
Successfully compiled "resources/public/js/compiled/app.js" in 19.139 seconds.
Figwheel: Starting CSS Watcher for paths  ["resources/public/css"]
nil
user=> (cljs)
Launching ClojureScript REPL for build: dev
[...]
```

Some time after the above, open your browser to [http://localhost:3449/](http://localhost:3449/).
After it connects you'll get your `user=>` prompt back, and you should be able to
`(.alert js/window "Whoopee!")` and see the result in your browser.

### Run tests:

```
lein with-profile test doo phantom test
```

This runs the tests and then watches for chages to files after which it re-runs them.
Append `once` to the end to run the tests a single time.

## Production Build

To compile an advanced clojurescript build:

```
lein clean
lein with-profile prod cljsbuild once min
```

## Deployment

The site is auto-deployed to [netlify](https://www.netlify.com) on commits 
to master.
