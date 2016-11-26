(ns haunting-refrain.runner
    (:require [doo.runner :refer-macros [doo-tests doo-all-tests]]
              haunting-refrain.core-test
              haunting-refrain.test.foursquare
              haunting-refrain.test.playlist))

(doo-tests 'haunting-refrain.core-test
           'haunting-refrain.test.foursquare
           'haunting-refrain.test.playlist)
