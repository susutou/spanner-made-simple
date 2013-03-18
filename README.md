Spanner Made Simple
===================

This is a course project for CS274 (Winter 2013) in UCSB, given by Prof. [Divyakant Agrawal](http://www.cs.ucsb.edu/~agrawal/).
We are trying to implement the replication and transactional part of Google Spanner, described in the recently published
[paper](http://static.googleusercontent.com/external_content/untrusted_dlcp/research.google.com/en/us/archive/spanner-osdi2012.pdf).

Overview
--------
Suppose we have 3 replicas, and each replica is partitioned into 2 shards.
Partitions in different replicas with the same key range form a Paxos group, and for each Paxos group there is a leader.
In this case, we have 2 leaders, and the 2 leaders are coordinated with a 2-Phase Commit (2PC) protocol.

What we need to do is to feed the system with a sequence of operations, and replicate these operations in order to all replicas.
We need to provide the Paxos log and operation log at last to see the correctness of the replication.