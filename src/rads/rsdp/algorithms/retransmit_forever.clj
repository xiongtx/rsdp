(ns rads.rsdp.retransmit-forever
  "Algorithm 2.1: Retransmit Forever

  Module:
    - Name: StubbornPointToPointLinks, instance sl.

  Events:
    - Request: ⟨ sl, Send | q, m ⟩
      - Requests to send message m to process q.
    - Indication: ⟨ sl, Deliver | p, m ⟩
      - Delivers message m sent by process p.

  Properties:
    - SL1: Stubborn delivery
      - If a correct process p sends a message m once to a correct process q,
        then q delivers m an infinite number of times.
    - SL2: No creation
      - If some process q delivers a message m with sender p, then m was
        previously sent to q by process p."
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
