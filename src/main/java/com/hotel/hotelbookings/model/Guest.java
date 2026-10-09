/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.hotelbookings.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sashasabeta
 */
public class Guest extends Person {

    private List<Booking> bookingHistory;
    public Guest(String name, String id, String number, String email, int loyaltyPoints){
        super(name, id, number, email);
        this.loyaltyPoints =  loyaltyPoints;
        this.bookingHistory = new ArrayList<Booking>();
    }
    private int loyaltyPoints;
    
     @Override       
    public String getRole()
    {
        return "Guest";
    }

    
    public List<Booking> getBookingHistory()
    {
        return this.bookingHistory;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }
    public void setLoyaltyPoints(int loyaltypoints){
        this.loyaltyPoints = loyaltypoints;
    }
    public void addBooking( Booking booking)
    {
        bookingHistory.add(booking);
    }
}
