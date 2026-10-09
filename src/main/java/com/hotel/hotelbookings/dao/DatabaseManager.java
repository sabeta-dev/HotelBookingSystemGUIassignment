/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.hotelbookings.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author sashasabeta
 */
public class DatabaseManager {

    
    
    
    private static final String url = "jdbc:derby:HotelDB;create=true";
    
    Connection conn;
    private DatabaseManager() {
        establishHotelDBconnection();
        createTableIfMissing();
    }
    
    private static DatabaseManager instance;
   
    public static DatabaseManager getInstance()
    {
        if(instance == null){
            instance = new DatabaseManager();
        }
        return instance;
        
    }
    
    public void establishHotelDBconnection()
    {
        try{
            conn = DriverManager.getConnection(url);
            System.out.println(url+ "  connected...");
        }
        catch (SQLException ex){
            throw new IllegalStateException("Could not connect to database", ex);  
        }
    }
    public Connection getConnection ()
    {
        return this.conn;
    }    
    
    public boolean tableExists(String tableName)
    {
        
        try{
            DatabaseMetaData tableData = conn.getMetaData();
        
            try( ResultSet rs = tableData.getTables(null, null, tableName.toUpperCase(), null)){
            return rs.next();
            }
        }
        catch (SQLException ex) {
            throw new IllegalStateException("Could not check for table " + tableName, ex);

        }
        
             
    }
    public void createTableIfMissing()
    {
        try(Statement statement = conn.createStatement()){
            if (!tableExists("ROOMS"))
            {
                statement.executeUpdate(
                "CREATE TABLE ROOMS "+" "
                        + "(ROOM_NUMBER INT PRIMARY KEY,"+
                        "ROOMTYPE VARCHAR(20) NOT NULL,"+
                        "PRICE DOUBLE NOT NULL, CAPACITY INT NOT NULL, "
                        + "AVAILABLE BOOLEAN NOT NULL)");
            }
             
        }
        catch (SQLException ex){
            throw new IllegalStateException("Could not create tables", ex); 
        }
    }
    public void closeConnection()
    {
        if (conn != null)
        {
            try{
                conn.close();
            }
            catch(SQLException ex){
                System.err.println("Could not close connection: "+ex.getMessage());
            }
            conn = null;
            instance = null;
            
        }
    }
    
    
}
