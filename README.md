# Search-Engine


Introduction

this assignment will be simulating the functionality of a search engine. It will implement both the client and server side.
The assignment puts an emphasis on multi threaded programming and the synchronization of these.

Preliminary description

We distinguish two entities. A client program and a server program.
Each operates in a separate process. Throughout there is only one server
process and there is no bound on the number of client processes (which
is the situation that say Google has to face). The client processes do not
communicate with one another. All communication is conducted between
the server and its clients. The communication is done through (network)
sockets.
Roughly speaking, a client process sends search requests or queries to the
server through a predefined socket in an infinite loop. Each client request
is processed by the server which returns a reply. In our small simulation a
search request will simply be a number chosen at random from some range.
The response will also be a number. Below we make this precise. Let us
do mention already here that we will not draw the numbers uniformly at
random. Instead we will have prescribed probabilities for each number in the
range.
The server keeps a database of all requests and their replies. To simulate
this database we shall use hash table, That is, the server maintains a hash table
in which it saves triples of numbers (x; y; z) where x is the request,
y is its reply, and z is the number of times that x was requested so far. Let
us explain the need for z. The server keeps track of z in order to maintain
a cache of most frequently asked requests and replies. The cache is kept in
the main memory making queries to it much faster. If the
cache is maintained in such a way that frequently requested numbers are kept
in the cache then querying the cache makes it possible to avoid searching the
database files repeatedly. Indeed, repeated queries to the files will harm the
performance of the system. This will be made precise below.
During examination we shall first run the server process and then we will
create an arbitrary number of client processes. The server listens on a single
socket port for client queries/search requests. All clients send their search
queries/requests through this single port throughout the application. The
server may listen on other ports for other purposes, say for various logistical
purposes; however, for search queries there is only one port.
For each incoming search request the server assigns a separate thread to
handle the request and return the reply. We refer to the latter as a search
thread (S-thread, hereafter). Naturally, we cannot allow the server to create
a large number of S-threads. Hence, we shall use a thread pool manager
to organise and handle these server threads. The number of S-thread the server is allowed to create is S,
where S is a parameter passed to the server process through the command
line upon its creation.
