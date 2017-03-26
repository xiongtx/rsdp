(ns rads.rsdp.algorithms.fair-loss-links
  "Module:
    Name: FairLossPointToPointLinks, instance fll.

  Events:
    - Request: ⟨ fll, Send | q, m ⟩
      - Requests to send message m to process q.
    - Indication: ⟨ fll, Deliver | p, m ⟩
      - Delivers message m sent by process p.

  Properties:
    - FLL1: Fair-loss
      - If a correct process p infinitely often sends a message m to a correct
        process q, then q delivers m an infinite number of times.
    - FLL2: Finite duplication
      - If a correct process p sends a message m a finite number of times to
        process q, then m cannot be delivered an infinite number of times by q.
    - FLL3: No creation
      - If some process q delivers a message m with sender p, then m was
        previously sent to q by process p."
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
