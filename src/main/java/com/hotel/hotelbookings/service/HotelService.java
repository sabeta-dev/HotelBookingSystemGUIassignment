/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.hotelbookings.service;

/**
 *
 * @author sashasabeta
 */
import com.hotel.hotelbookings.model.Booking;
import com.hotel.hotelbookings.model.Guest;
import com.hotel.hotelbookings.model.Room;
import java.util.List;

public interface HotelService {

    List<Room> findAvailableRooms();

    int calculateNights(String checkIn, String checkOut);

    double calculateBookingCost(Booking booking);

    Booking createBooking(String guestId, int roomNumber, String checkIn, String checkOut);

    void cancelBooking(String bookingId);

    Guest addGuest(String id, String name, String phone, String email);

    public void updateBooking(String bookingId, String newCheckIn, String newCheckOut);

    void updateBookingStatus(String bookingId, String newStatus);
}
