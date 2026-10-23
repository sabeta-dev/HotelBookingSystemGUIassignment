/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.hotelbookings.dao;

import com.hotel.hotelbookings.model.Booking;
import java.util.List;


/**
 *
 * @author sashasabeta
 */
public interface BookingDAO {

    List<Booking> findAll();

    Booking findById(String bookingId);

    List<Booking> findByGuestId(String guestId);

    void add(Booking booking);

    void updateStatus(String bookingId, String status);
    
    void updateDates(String bookingId, String checkIn, String checkOut);

    void delete(String bookingId);
    
    
}
