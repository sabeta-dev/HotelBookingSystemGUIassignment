/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.hotel.hotelbookings.model;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sashasabeta
 */
public class RoomFactoryTest {

    public RoomFactoryTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void standardRoomTest() {
        System.out.println("Standard room test:");
        String roomType = "Standard";
        int roomNumber = 101;
        double price = 100.0;
        int capacity = 2;
        Room expResult = null;
        Room result = RoomFactory.createRoom("Standard", 101, 100.0, 2);
        assertEquals(300, result.calculatePrice(3), 0.1);
        assertEquals(101, result.getRoomNumber());
        assertTrue(result.getIsAvailable());
        // TODO review the generated test code and remove the default call to fail.
    }

    @Test(expected = AssertionError.class)
    public void deluxeRoomWrongPercentTest() {
        System.out.println("Deluxe room test:");
        String roomType = "Deluxe";
        int roomNumber = 201;
        double price = 150.0;
        int capacity = 4;
        Room expResult = null;
        Room result = RoomFactory.createRoom("Deluxe", 201, 150.0, 4);
        assertEquals(540, result.calculatePrice(3), 0.1);
        assertEquals(201, result.getRoomNumber());
        assertTrue(result.getIsAvailable());
        // TODO review the generated test code and remove the default call to fail.
    }

    @Test
    public void suiteRoomTest() {
        System.out.println("Deluxe room test:");
        String roomType = "Deluxe";
        int roomNumber = 301;
        double price = 200.0;
        int capacity = 5;
        Room expResult = null;
        Room result = RoomFactory.createRoom("Suite", 301, 200.0, 4);
        assertEquals(750, result.calculatePrice(3), 0.1);
        assertEquals(301, result.getRoomNumber());
        assertTrue(result.getIsAvailable());
        // TODO review the generated test code and remove the default call to fail.   
    }

    @Test
    public void deluxeRoomTest() {
        Room result = RoomFactory.createRoom("Deluxe", 202, 150.0, 4);
        assertEquals(517.5, result.calculatePrice(3), 0.01);
        assertEquals(202, result.getRoomNumber());
        assertTrue(result.getIsAvailable());
    }

    @Test(expected = IllegalArgumentException.class)
    public void unknownRoomTypeIsRejected() {
        RoomFactory.createRoom("Penthouse", 104, 100.0, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullRoomTypeIsRejected() {
        RoomFactory.createRoom(null, 105, 100.0, 2);
    }

}
