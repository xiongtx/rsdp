(ns rads.rsdp.job-transformation-by-buffering
  "Algorithm 1.3: Job-Transformation by Buffering
  
  Module:
    Name: TransformationHandler, instance th.

  Events:
    Request: ⟨ th, Submit | job ⟩
      - Submits a job for transformation and for processing.
    Indication: ⟨ th, Confirm | job ⟩
      - Confirms that the given job has been (or will be) transformed and processed.
    Indication: ⟨ th, Error | job ⟩
      - Indicates that the transformation of the given job failed.

  Properties:
    - TH1: Guaranteed response: Every submitted job is eventually confirmed or its
           transformation fails.
    - TH2: Soundness: A submitted job whose transformation fails is not processed.")
