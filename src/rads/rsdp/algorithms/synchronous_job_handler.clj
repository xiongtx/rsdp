(ns rads.rsdp.algorithms.synchronous-job-handler
  "Algorithm 1.1: Synchronous Job Handler
  
  Module:
    - Name: JobHandler, instance jh.

  Events:
    - Request: ⟨ jh, Submit | job ⟩
      - Requests a job to be processed.
    - Indication:⟨ jh, Confirm | job ⟩
      - Confirms that the given job has been (or will be) processed.

  Properties:
    - JH1: Guaranteed response
      - Every submitted job is eventually confirmed.")
