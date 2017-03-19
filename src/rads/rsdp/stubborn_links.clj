(ns rads.rsdp.stubborn-links
  (:require
    [clojure.core.match :refer [match]]
    [rads.rsdp.util :as util]))

(def delta 3000)

(defn- new-handler [sent]
  (fn [{sl :pid {fll :pid} :fll} event trigger]
    (match [event]
      [[sl :init]] (do
                     (reset! sent #{})
                     (util/start-timer delta))
      [[:timeout]] (do
                     (doseq [[q m] @sent]
                       (trigger [fll :send q m]))
                     (util/start-timer delta))
      [[sl :send q m]] (do
                         (trigger [fll :send q m])
                         (swap! sent conj [q m]))
      [[fll :deliver p m]] (trigger [sl :deliver p m])
      :else nil)))

(defn new-stubborn-link [opts]
  (util/new-async-process
    (assoc opts :handler (new-handler (atom nil)))))
