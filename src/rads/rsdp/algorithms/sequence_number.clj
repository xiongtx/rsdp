(ns rads.rsdp.algorithms.sequence-number
  "Algorithm 2.11: Sequence Number
  
  Module:
    - Name: FIFOPerfectPointToPointLinks, instance fpl.

  Events:
    - Request: ⟨ fpl, Send | q, m ⟩
      - Requests to send message m to process q.
    - Indication: ⟨ fpl, Deliver | p, m ⟩
      - Delivers message m sent by process p.

  Properties:
    - FPL1–FPL3:
      - Same as properties PL1–PL3 of perfect point-to-point links
        (Module 2.3).
    - FPL4: FIFO delivery
      - If some process sends message m1 before it sends message m2, then no
        correct process delivers m2 unless it has already delivered m1.")
