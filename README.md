# rsdp

**This code is alpha status. The current implementations are not necessarily functional or correct.**

Implementations of algorithms from [*Introduction to Reliable and Secure Distributed Programming*][rsdp].

[rsdp]: http://distributedprogramming.net

## List of Algorithms

### Chapter 1

- [1.1 Synchronous Job Handler](https://github.com/rads/rsdp/blob/master/src/rads/rsdp/algorithms/synchronous_job_handler.clj)
- [1.2 Asynchronous Job Handler](https://github.com/rads/rsdp/blob/master/src/rads/rsdp/algorithms/asynchronous_job_handler.clj)
- [1.3 Job-Transformation by Buffering](https://github.com/rads/rsdp/blob/master/src/rads/rsdp/algorithms/job_transformation_by_buffering.clj)

### Chapter 2

- [2.1 Retransmit Forever](https://github.com/rads/rsdp/blob/master/src/rads/rsdp/algorithms/retransmit_forever.clj)
- [2.2 Eliminate Duplicates](https://github.com/rads/rsdp/blob/master/src/rads/rsdp/algorithms/eliminate_duplicates.clj)
- [2.3 Log Delivered](https://github.com/rads/rsdp/blob/master/src/rads/rsdp/algorithms/log_delivered.clj)
- 2.4 Authenticate and Filter
- 2.5 Exclude on Timeout
- 2.6 Monarchical Leader Election
- 2.7 Increasing Timeout
- 2.8 Monarchical Eventual Leader Detection
- 2.9 Elect Lower Epoch
- 2.10 Rotating Byzantine Leader Detection
- 2.11 Sequence Number
