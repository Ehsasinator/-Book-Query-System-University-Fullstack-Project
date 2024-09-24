package com.example.ass4;

public class Credentials {
    //JDBC connection
    public static final String USERNAME = "ehsanjaved";
    public static final String PASSWORD = "4Placed53";
    public static final String URL = "jdbc:postgresql://localhost:5432/FSAD2023_Books";
    //Client-server connection
    public static final String HOST = "127.0.0.1"; //localhost
    public static final int PORT = 9994; //This is NOT the port in postgres, but the port at which the BooksDatabaseServer is listening
}
