package com.example.ass4;/*
 * BooksDatabaseService.java
 *
 * The service threads for the books database server.
 * This class implements the database access service, i.e. opens a JDBC connection
 * to the database, makes and retrieves the query, and sends back the result.
 *
 * author: 2434756
 *
 */

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
//import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;

import java.net.Socket;

import java.util.StringTokenizer;

import java.sql.*;
import javax.sql.rowset.*;
    //Direct import of the classes CachedRowSet and CachedRowSetImpl will fail becuase
    //these clasess are not exported by the module. Instead, one needs to impor
    //javax.sql.rowset.* as above.



public class BooksDatabaseService extends Thread{

    private Socket serviceSocket = null;
    private String[] requestStr  = new String[2]; //One slot for author's name and one for library's name.
    private ResultSet outcome   = null;

	//JDBC connection
    private String USERNAME = Credentials.USERNAME;
    private String PASSWORD = Credentials.PASSWORD;
    private String URL      = Credentials.URL;



    //Class constructor
    public BooksDatabaseService(Socket aSocket){
        this.serviceSocket = aSocket;
        this.start();
		//TO BE COMPLETED
		
    }


    //Retrieve the request from the socket
    public String[] retrieveRequest()
    {
        this.requestStr[0] = ""; //For author
        this.requestStr[1] = ""; //For library
		
		String tmp = "";
        try {
            InputStream inputStream = serviceSocket.getInputStream();
            InputStreamReader streamReader = new InputStreamReader(inputStream);
            StringBuffer stringBuffer = new StringBuffer();
            char val;
            while (true)
            {
                val = (char) streamReader.read();
                if (val == '#')
                    break;
                stringBuffer.append(val);
            }
            tmp = stringBuffer.toString();
            String[] split = tmp.split(";");
            this.requestStr[0] = split[0];
            this.requestStr[1] = split[1];
         }catch(IOException e){
            System.out.println("Service thread " + this.getId() + ": " + e);
        }
        return this.requestStr;
    }


    //Parse the request command and execute the query
//    public boolean attendRequest()
//    {
//        boolean flagRequestAttended = true;
//
//		this.outcome = null;
//
//		String sql = "SELECT book.title, book.publisher, book.genre, book.rrp, COUNT(bookcopy.*) as copy_count\n" +
//                "FROM book \n" +
//                "INNER JOIN bookcopy  ON book.bookid = bookcopy.bookid\n" +
//                "INNER JOIN author  ON book.authorid = author.authorid\n" +
//                "INNER JOIN library  ON bookcopy.libraryid = library.libraryid\n" +
//                "WHERE author.familyname = ? AND library.city = ?\n" +
//                "GROUP BY book.title, book.publisher, book.genre, book.rrp;"; //TO BE COMPLETED- Update this line as needed.
//
//
//		try {
//			//Connet to the database
//            Class.forName("org.postgresql.Driver");
//            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
//			//TO BE COMPLETED
//
//			//Make the query
//            PreparedStatement pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//            pstmt.clearParameters();
//            //PreparedStatement stmt = conn.prepareStatement(sql);
//            pstmt.setString(1,this.requestStr[0]);
//            pstmt.setString(2,this.requestStr[1]);
//			//TO BE COMPLETED
//
//			//Process query
//            ResultSet rs = pstmt.executeQuery();
//            String title = rs.getObject("title").toString();
//            String publisher = rs.getObject("publisher").toString();
//            String genre = rs.getObject("genre").toString();
//            String rrp = rs.getObject("rrp").toString();
//            String copyCount = rs.getObject("copy_count").toString();
//
//            String output = String.format("%s | %s | %s | %s | %s", title, publisher, genre, rrp, copyCount);
//            System.out.println(output);
//            }
//            rs.beforeFirst();
//            CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
//            crs.populate(rs);
//
//            this.outcome = crs;
//            crs.beforeFirst();
//			//TO BE COMPLETED -  Watch out! You may need to reset the iterator of the row set.
//            rs.close();
//            pstmt.close();
//            conn.close();
//			//Clean up
//			//TO BE COMPLETED
//
//		} catch (Exception e)
//		{ System.out.println(e); }
//
//        return flagRequestAttended;
//    }

    public boolean attendRequest() {
        boolean flagRequestAttended = true;
        this.outcome = null;
        String sql = "SELECT book.title, book.publisher, book.genre, book.rrp, COUNT(bookcopy.*) as copy_count\n" +
                "FROM book \n" +
                "INNER JOIN bookcopy  ON book.bookid = bookcopy.bookid\n" +
                "INNER JOIN author  ON book.authorid = author.authorid\n" +
                "INNER JOIN library  ON bookcopy.libraryid = library.libraryid\n" +
                "WHERE author.familyname = ? AND library.city = ?\n" +
                "GROUP BY book.title, book.publisher, book.genre, book.rrp;"; //TO BE COMPLETED- Update this line as needed.

        try {
            // Connect to the database
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // Make the query
            PreparedStatement pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstmt.clearParameters();
            pstmt.setString(1, this.requestStr[0]);
            pstmt.setString(2, this.requestStr[1]);

            // Process query
            ResultSet rs = pstmt.executeQuery();
            String title = "";
            String publisher = "";
            String genre = "";
            String rrp = "";
            String copyCount = "";

            if (rs.next()) {
                title = rs.getObject("title").toString();
                publisher = rs.getObject("publisher").toString();
                genre = rs.getObject("genre").toString();
                rrp = rs.getObject("rrp").toString();
                copyCount = rs.getObject("copy_count").toString();

                String output = String.format("%s | %s | %s | %s | %s", title, publisher, genre, rrp, copyCount);
                System.out.println(output);

                rs.beforeFirst();
                CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
                crs.populate(rs);
                this.outcome = crs;
                crs.beforeFirst();
            }

            // Clean up
            rs.close();
            pstmt.close();
            conn.close();

        } catch (Exception e) {
            System.out.println(e);
            flagRequestAttended = false;
        }

        return flagRequestAttended;
    }


    //Wrap and return service outcome
    public void returnServiceOutcome(){
        try {
			//Return outcome
			//TO BE COMPLETED
            ObjectOutputStream oos = new ObjectOutputStream(this.serviceSocket.getOutputStream());
            oos.writeObject(this.outcome);
            oos.flush();
			
            System.out.println("Service thread " + this.getId() + ": Service outcome returned; " + this.outcome);
            this.serviceSocket.close();
			//Terminating connection of the service socket
			//TO BE COMPLETED
			
			
        }catch (IOException e){
            System.out.println("Service thread " + this.getId() + ": " + e);
        }
    }


    //The service thread run() method
    public void run()
    {
		try {
			System.out.println("\n============================================\n");
            //Retrieve the service request from the socket
            this.retrieveRequest();
            System.out.println("Service thread " + this.getId() + ": Request retrieved: "
						+ "author->" + this.requestStr[0] + "; library->" + this.requestStr[1]);

            //Attend the request
            boolean tmp = this.attendRequest();

            //Send back the outcome of the request
            if (!tmp)
                System.out.println("Service thread " + this.getId() + ": Unable to provide service.");
            this.returnServiceOutcome();

        }catch (Exception e){
            System.out.println("Service thread " + this.getId() + ": " + e);
        }
        //Terminate service thread (by exiting run() method)
        System.out.println("Service thread " + this.getId() + ": Finished service.");
    }

}
