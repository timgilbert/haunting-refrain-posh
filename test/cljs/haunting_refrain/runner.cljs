(ns haunting-refrain.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [haunting-refrain.core-test]))

(doo-tests 'haunting-refrain.core-test)
