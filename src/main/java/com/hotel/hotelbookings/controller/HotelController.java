/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.hotelbookings.controller;

import com.hotel.hotelbookings.dao.BookingDAO;
import com.hotel.hotelbookings.dao.GuestDAO;
import com.hotel.hotelbookings.dao.RoomDAO;
import com.hotel.hotelbookings.model.Booking;
import com.hotel.hotelbookings.model.Guest;
import com.hotel.hotelbookings.model.Room;
import com.hotel.hotelbookings.service.HotelService;
import java.util.List;

/**
 *
 * @author sashasabeta
 */
public class HotelController {

    private final HotelService service;
    private final RoomDAO roomDAO;
    private final GuestDAO guestDAO;
    private final BookingDAO bookingDAO;

    public HotelController(HotelService service, RoomDAO roomDAO, GuestDAO guestDAO, BookingDAO bookingDAO) {
        this.service = service;
        this.roomDAO = roomDAO;
        this.guestDAO = guestDAO;
        this.bookingDAO = bookingDAO;
    }

    public List<Room> getAllRooms() {
        return roomDAO.findAll();
    }

    public List<Guest> getAllGuests() {
        return guestDAO.findAll();
    }

    public List<Booking> getAllBookings() {
        return bookingDAO.findAll();
    }

    public Guest addGuest(String id, String name, String phone, String email) {
        return service.addGuest(id, name, phone, email);
    }

    public Booking createBooking(String guestId, int roomNumber, String checkIn, String checkOut) {
        return service.createBooking(guestId, roomNumber, checkIn, checkOut);
    }

    public void cancelBooking(String bookingId) {
        service.cancelBooking(bookingId);
    }

    public double getBookingCost(Booking booking) {
        return service.calculateBookingCost(booking);
    }
}
