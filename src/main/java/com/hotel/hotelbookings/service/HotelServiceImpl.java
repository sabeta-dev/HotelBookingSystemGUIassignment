/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.hotelbookings.service;

/**
 *
 * @author sashasabeta
 */
import com.hotel.hotelbookings.dao.BookingDAO;
import com.hotel.hotelbookings.dao.GuestDAO;
import com.hotel.hotelbookings.dao.RoomDAO;
import com.hotel.hotelbookings.exception.BookingException;
import com.hotel.hotelbookings.exception.ValidationException;
import com.hotel.hotelbookings.model.Booking;
import com.hotel.hotelbookings.model.Guest;
import com.hotel.hotelbookings.model.Room;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class HotelServiceImpl implements HotelService {

    private final RoomDAO roomDAO;
    private final GuestDAO guestDAO;
    private final BookingDAO bookingDAO;

    public HotelServiceImpl(RoomDAO roomDAO, GuestDAO guestDAO, BookingDAO bookingDAO) {
        this.roomDAO = roomDAO;
        this.guestDAO = guestDAO;
        this.bookingDAO = bookingDAO;
    }

    @Override
    public List<Room> findAvailableRooms() {
        return roomDAO.findAvailable();
    }

    @Override
    public int calculateNights(String checkIn, String checkOut) {
        LocalDate in = parseDate(checkIn, "Check-in");
        LocalDate out = parseDate(checkOut, "Check-out");
        if (!out.isAfter(in)) {
            throw new ValidationException("Check-out date must be after check-in date");
        }
        return (int) ChronoUnit.DAYS.between(in, out);
    }

    @Override
    public double calculateBookingCost(Booking booking) {
        int nights = calculateNights(booking.getCheckInDate(), booking.getCheckOutDate());
        return booking.calculateTotalCost(nights);
    }

    @Override
    public Booking createBooking(String guestId, int roomNumber, String checkIn, String checkOut) {
        calculateNights(checkIn, checkOut); // validates the dates

        Guest guest = guestDAO.findById(guestId);
        if (guest == null) {
            throw new BookingException("Guest " + guestId + " not found");
        }
        Room room = roomDAO.findByRoomNumber(roomNumber);
        if (room == null) {
            throw new BookingException("Room " + roomNumber + " not found");
        }
        if (!room.getIsAvailable()) {
            throw new BookingException("Room " + roomNumber + " is not available");
        }

        Booking booking = new Booking(nextBookingId(), checkIn.trim(), checkOut.trim(), guest, room);
        bookingDAO.add(booking);
        room.setIsAvailable(false);
        roomDAO.update(room);
        return booking;
    }

    @Override
    public void cancelBooking(String bookingId) {
        Booking booking = bookingDAO.findById(bookingId);
        if (booking == null) {
            throw new BookingException("Booking " + bookingId + " not found");
        }
        if ("Cancelled".equals(booking.getStatus())) {
            throw new BookingException("Booking " + bookingId + " is already cancelled");
        }
        bookingDAO.updateStatus(bookingId, "Cancelled");
        Room room = booking.getRoom();
        room.setIsAvailable(true);
        roomDAO.update(room);
    }

    @Override
    public Guest addGuest(String id, String name, String phone, String email) {
        if (id == null || id.trim().isEmpty() || id.trim().length() > 10) {
            throw new ValidationException("Guest ID is required (maximum 10 characters)");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Guest name is required");
        }
        if (phone == null || phone.trim().isEmpty()) {
            throw new ValidationException("Phone number is required");
        }
        if (email == null || !email.contains("@") || !email.contains(".")) {
            throw new ValidationException("A valid email address is required");
        }
        Guest guest = new Guest(name.trim(), id.trim(), phone.trim(), email.trim(), 0);
        guestDAO.add(guest);
        return guest;
    }

    private LocalDate parseDate(String text, String label) {
        if (text == null || text.trim().isEmpty()) {
            throw new ValidationException(label + " date is required");
        }
        try {
            return LocalDate.parse(text.trim());
        } catch (DateTimeParseException ex) {
            throw new ValidationException(label + " date must be in the format yyyy-MM-dd: " + text);
        }
    }

    private String nextBookingId() {
        int max = 0;
        for (Booking b : bookingDAO.findAll()) {
            String digits = b.getBookingId().replaceAll("\\D", "");
            if (!digits.isEmpty() && digits.length() <= 6) {
                max = Math.max(max, Integer.parseInt(digits));
            }
        }
        return String.format("B%03d", max + 1);
    }
}