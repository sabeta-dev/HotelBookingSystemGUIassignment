package com.hotel.hotelbookings.model;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author sashasabeta
 */
public abstract class Room {

    private int roomNumber;
    private double pricePerNight;
    private boolean isAvailable;
    private int capacity;

    public Room(int roomNumber, double pricePerNight, int capacity) {
        this.roomNumber = roomNumber;
        this.pricePerNight = pricePerNight;
        this.capacity = capacity;
        isAvailable = true;

    }

    public abstract double calculatePrice(int nights);

    public abstract String getRoomDetails();

    public int getRoomNumber() {
        return roomNumber;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public boolean getIsAvailable() {
        return isAvailable;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setIsAvailable(boolean available) {
        isAvailable = available;
        
    }
    public abstract String getRoomType();
}
