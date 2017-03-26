(ns rads.rsdp.algorithms.exclude-on-timeout
  "Algorithm 2.5: Exclude on Timeout

  Module:
    - Name: PerfectFailureDetector, instance P.

  Events:
    - Indication: ⟨ P, Crash | p ⟩
      - Detects that process p has crashed.

  Properties:
    - PFD1: Strong completeness: Eventually, every process that crashes is
      permanently detected by every correct process.
    - PFD2: Strong accuracy: If a process p is detected by any process, then p
      has crashed.")
