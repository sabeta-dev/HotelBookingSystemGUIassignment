/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.hotelbookings.dao;

/**
 *
 * @author sashasabeta
 */
public class DatabaseManagerTest {

    public static void main(String[] args) {
        DatabaseManager db = DatabaseManager.getInstance();
        System.out.println("Connection: " + db.getConnection());
        System.out.println("ROOMS exists: " + db.tableExists("ROOMS"));
        System.out.println("BOOKINGS exists: " + db.tableExists("BOOKINGS"));
        System.out.println("GUESTS exists: " + db.tableExists("GUESTS"));
        db.closeConnection();
    }
    }
