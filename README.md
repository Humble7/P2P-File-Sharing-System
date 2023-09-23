# P2P-File-Sharing-System
A Simple Napster Style Peer to Peer File Sharing System.

This project serves two main purposes: firstly, to familiarize you with concepts such as sockets, processes, threads, and makefiles; and secondly, to help you gain insight into the design and internals of a Napster-style peer-to-peer (P2P) file sharing system.

You have the creative freedom to implement this project in either C, C++, or Java programming languages. You are encouraged to utilize abstractions like sockets and threads (RPCs, RMIs, or web services are not allowed). Additionally, you are free to use any computer for development, but the evaluation must be carried out using Ubuntu Linux 22.04 LTS. Your assignment will be graded in a Linux environment, and points will be deducted if your programming assignment does not compile and run correctly.

In this project, your task is to design a straightforward P2P system with two components:

1. **Central Indexing Server:** This server indexes the content of all peers that register with it and provides a search facility to peers. In this simplified version, complex search algorithms are not required; exact matching is sufficient. It's important to note that the central index stores metadata about the files on the peers but not the actual data. At a minimum, the server should offer the following interface to peer clients:
   - `registry(peer id, file name, ...)`: Invoked by a peer to register all its files with the indexing server. The server then builds the index for the peer.
   - `search(file name)`: This procedure searches the index and returns all the matching peers to the requester.

2. **Peer:** A peer serves as both a client and a server. As a client, users specify a file name to the indexing server using "lookup." The indexing server returns a list of all other peers that have the requested file. Users can then choose one of these peers, and the client connects to it to download the file. As a server, the peer listens for requests from other peers and sends the requested file when receiving a request. At a minimum, the peer server should provide the following interface to the peer client:
   - `obtain(file name)`: Invoked by a peer to download a file from another peer.

This project offers an opportunity to gain hands-on experience with network programming and P2P system design.

### Prerequisites
Before running the program, ensure you have the following dependencies installed:
- JDK and Java

### Installation
1. Clone this repository to your local machine
2. Navigate to the project directory 

### Usage

1. Open a terminal and navigate to the project directory: `cd P2P-File-Sharing-System`
2. Run the program using the following command:
   a. `make run`
   b. enter `1` to launch the indexing server.
3. Open another terminal and navigate to the project directory: `cd P2P-File-Sharing-System`
4. Run the program using the following command:
   a. `make run`
   b. enter `2` to launch the client 1.
   c. register file into server: `shared-file-center/dir1`
   d. set up port number: `1111`
   e. set up peer ID: `1`
3. Open another terminal and navigate to the project directory: `cd P2P-File-Sharing-System`
4. Run the program using the following command:
   a. `make run`
   b. enter `2` to launch the client 2.
   c. register file into server: `shared-file-center/dir2`
   d. set up port number: `1112`
   e. set up peer ID: `2`
   f. search file name: `1_1.txt`
   g. enter the port number of the file server: `1111`
   h. enter the peer id of the file server: `1`
   i. 1_1.txt has been downloaded into shared-file-center/dir2

