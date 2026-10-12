package com.hotel.hotelbookings.model;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author sashasabeta
 */
public class DeluxeRoom extends Room {

    public DeluxeRoom(int roomNumber, double pricePerNight, int capacity) {
        super(roomNumber, pricePerNight, capacity);
    }

    

    @Override
    public double calculatePrice(int nights) {
        return Math.round(nights * (getPricePerNight()*(1+(0.15)))*100)/100.0;

    }

    @Override
    public String getRoomType() {
        return "Deluxe";
    }
    
    public String getRoomDetails() {
        return "Deluxe room("+ getRoomNumber()+"), "  + getIsAvailable() + ", this room has a capacity of " + getCapacity() + " available for $" + getPricePerNight() + " per night";
    }
    

}    

