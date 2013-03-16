Spanner Made Simple
===================

This is a course project for CS274 (Winter 2013) in UCSB, given by Prof. [Divyakant Agrawal](http://www.cs.ucsb.edu/~agrawal/).
We are trying to implement the replication and transactional part of Google Spanner, described in the recently published
[paper](http://static.googleusercontent.com/external_content/untrusted_dlcp/research.google.com/en/us/archive/spanner-osdi2012.pdf).

Overview
========
To our understanding, this part is basically a 2PC (2-Phase Commit) over Paxos replication.