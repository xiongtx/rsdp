(ns rads.rsdp.algorithms.monarchical-leader-election
  "Algorithm 2.6: Monarchical Leader Election

  Module:
    - Name: LeaderElection, instance le.
  
  Events:
    - Indication: ⟨ le, Leader | p ⟩
      - Indicates that process p is elected as leader.

  Properties:
    - LE1: Eventual detection
      - Either there is no correct process, or some correct process is
        eventually elected as the leader.
    - LE2: Accuracy
      - If a process is leader, then all previously elected leaders have
        crashed.")
