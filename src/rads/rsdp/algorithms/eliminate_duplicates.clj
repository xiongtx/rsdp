(ns rads.rsdp.eliminate-duplicates
  (:require
    [clojure.core.match :refer [match]]
    [rads.rsdp.util :as util]))

(defn- new-handler [delivered]
  (fn [{pl :pid {sl :pid} :sl} event trigger]
    (match [event]
      [[pl :init]] (reset! delivered #{})
      [[pl :send q m]] (trigger [sl :send q m])
      [[sl :deliver p m]] (when-not (@delivered m)
                            (swap! delivered conj m)
                            (trigger [pl :deliver p m]))
      :else nil)))

(defn new-perfect-link [opts]
  (util/new-async-process
    (assoc opts :handler (new-handler (atom nil)))))
