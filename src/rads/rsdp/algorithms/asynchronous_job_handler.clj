(ns rads.rsdp.algorithms.asynchronous-job-handler
  "Algorithm 1.2: Asynchronous Job Handler
  
  Module:
    - Name: JobHandler, instance jh.

  Events:
    - Request: ⟨ jh, Submit | job ⟩
      - Requests a job to be processed.
    - Indication:⟨ jh, Confirm | job ⟩
      - Confirms that the given job has been (or will be) processed.

  Properties:
    - JH1: Guaranteed response
      - Every submitted job is eventually confirmed."
  (:require [clojure.core.match :refer [match]]))

(defn advance [state event {:keys [jh process select-job] :as config}]
  (match [event]
    [[jh :init]] (assoc state ::buffer #{})
    [[jh :submit job]] (-> state
                           (update ::buffer conj job)
                           (assoc :trigger [[jh :confirm job]]))
    [:upon] (when (seq (::buffer state))
              (let [job (select-job (::buffer state))]
                (process job)
                (update state ::buffer disj job)))
    :else nil))
