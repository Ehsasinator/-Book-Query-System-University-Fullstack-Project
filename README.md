# Book Query System

This is a 3-tier, TCP-based, multi-threaded client-server application that allows users to query a PostgreSQL database for books by author and library location. The client features a JavaFX graphical interface, while the server processes client requests and retrieves data from the database via JDBC.

Features

Client-Server Architecture: TCP-based communication between a JavaFX client and a multi-threaded server.
JDBC Integration: Server connects to a PostgreSQL database using JDBC.
Parameterized Query: Queries books available from a given author in a specified library, returning details like title, publisher, genre, price, and copies available.
Multithreading: Server can handle multiple client requests simultaneously by spawning new service threads.
SQL Injection Protection: The server uses prepared statements to prevent SQL injection attacks.
Graceful Error Handling: Robust exception handling across all components ensures smooth execution or clean exit with appropriate error codes.
Query Details

The server performs the following query:

Given an author’s last name and a library’s city, retrieve a list of available books by that author in the specified library.
For each available book, the following details are provided:
Title
Publisher
Genre
Recommended Retailer Price
Number of Copies Available
Books with 0 copies available are not listed.
Components

Client: A JavaFX-based GUI allowing users to enter search parameters (author's last name and library city).
Main Server: Listens for client requests, creates a new service provider thread for each request.
Service Provider (Thread): Connects to the PostgreSQL database, executes the query, and sends results back to the client.
Database

The application connects to a PostgreSQL database and uses the postgresql-42.6.0.jar driver.

Exception Handling

Exceptions are handled locally at the point they occur.
Program exits with code 1 on critical failures.
Program exits with code 0 on successful execution.
Requirements

PostgreSQL 12+ with the postgresql-42.6.0.jar driver.
JavaFX for the client-side GUI.
Multi-threading support for the server.
Java 11+.
Running the Application

Set up a PostgreSQL database with the required schema.
Compile and run the server from the command line.
Run the client application and connect to the server to perform queries.
