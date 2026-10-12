/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.hotelbookings.model;

/**
 *
 * @author sashasabeta
 */
public class RoomFactory {

    public static Room createRoom(String roomType, int roomNumber, double price, int capacity) {
        Room r = null;
        if (roomType == null) {
            throw new IllegalArgumentException("Room type can not be null");
        }

        switch (roomType.toLowerCase()) {
            case "standard":
                r = new StandardRoom(roomNumber, price, capacity);
                break;

            case "deluxe":
                r = new DeluxeRoom(roomNumber, price, capacity);
                break;

            case "suite":
                r = new Suite(roomNumber, price, capacity);
                break;

            default:
                throw new IllegalArgumentException("Incorrect room type entered " + roomType);

        }
        return r;

    }
}
