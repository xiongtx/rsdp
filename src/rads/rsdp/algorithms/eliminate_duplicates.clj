(ns rads.rsdp.eliminate-duplicates
  "Algorithm 2.2: Eliminate Duplicates

  Module:
    - Name: PerfectPointToPointLinks, instance pl.

  Events:
    - Request: ⟨ pl, Send | q, m ⟩
      - Requests to send message m to process q.
    - Indication: ⟨ pl, Deliver | p, m ⟩
      - Delivers message m sent by process p.

  Properties:
    - PL1: Reliable delivery
      - If a correct process p sends a message m to a correct process q, then
        q eventually delivers m.
    - PL2: No duplication
      - No message is delivered by a process more than once.
    - PL3: No creation
      - If some process q delivers a message m with sender p, then m was
        previously sent to q by process p."
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
