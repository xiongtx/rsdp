(ns rads.rsdp.fair-loss-links
  (:require
    [clojure.core.match :refer [match]]
    [rads.rsdp.util :as util]))

(defn- new-handler []
  (fn [{fll :pid} event trigger]
    (match [event]
      [[fll :send p m]] (when (= 1 (rand-int 2))
                          (trigger [fll :deliver p m]))
      :else nil)))

(defn new-fair-loss-link [opts]
  (util/new-async-process
    (assoc opts :handler (new-handler))))
