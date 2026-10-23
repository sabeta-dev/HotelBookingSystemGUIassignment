/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.hotelbookings.model;

/**
 *
 * @author sashasabeta
 */
public class Booking {

    private String bookingId;
    private String checkInDate;
    private String checkOutDate;
    private Guest guest;
    private Room room;
    private String status;

    public Booking(String bookingId, String checkInDate, String checkOutDate, Guest guest, Room room) {
        this.bookingId = bookingId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.guest = guest;
        this.room = room;
        this.status = "Confirmed";
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public Guest getGuest() {
        return guest;
    }

    public Room getRoom() {
        return room;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }
    
    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

        
    public double calculateTotalCost(int nights)
    {
        return room.calculatePrice(nights);
    }
}
