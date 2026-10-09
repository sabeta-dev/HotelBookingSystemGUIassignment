package com.hotel.hotelbookings.model;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author sashasabeta
 */
public class StandardRoom extends Room {

    public StandardRoom(int roomNumber, double pricePerNight, int capacity) {
        super(roomNumber, pricePerNight, capacity);

    }

    @Override
    public double calculatePrice(int nights) {
        return Math.round(nights * getPricePerNight()*100)/100.0;

    }

        @Override
    public String getRoomType(){
        return "Standard";
    }

    public String getRoomDetails() {
        return "Standard room("+ getRoomNumber()+"), "  + getIsAvailable() + ", this room has a capacity of " + getCapacity() + " availble for $" + getPricePerNight() + " per night";
    }
    
}
