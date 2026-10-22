package com.hotel.hotelbookings.model;

import com.hotel.hotelbookings.dao.BookingDAO;
import com.hotel.hotelbookings.dao.BookingDAOImpl;
import com.hotel.hotelbookings.dao.DatabaseManager;
import com.hotel.hotelbookings.dao.GuestDAO;
import com.hotel.hotelbookings.dao.GuestDAOImpl;
import com.hotel.hotelbookings.dao.RoomDAO;
import com.hotel.hotelbookings.dao.RoomDAOImpl;
import com.hotel.hotelbookings.model.Booking;
import com.hotel.hotelbookings.model.Guest;
import com.hotel.hotelbookings.model.Room;
import com.hotel.hotelbookings.model.RoomFactory;
import java.util.List;

public class BookingDAOTest {

    private static final String BOOKING_ID = "TESTB1";
    private static final String GUEST_ID = "TESTG1";
    private static final int ROOM_NUMBER = 9998;

    public static void main(String[] args) {
        GuestDAO guestDAO = new GuestDAOImpl();
        RoomDAO roomDAO = new RoomDAOImpl();
        BookingDAO bookingDAO = new BookingDAOImpl(guestDAO, roomDAO);

        // 0. Clean up leftovers, in FK-safe order: booking, then guest and room
        cleanUp(bookingDAO, guestDAO, roomDAO);

        // 1. Add a guest and a room, then a booking that uses them
        Guest guest = new Guest("Booking Tester", GUEST_ID, "0211234567", "tester@example.com", 0);
        Room room = RoomFactory.createRoom("Suite", ROOM_NUMBER, 100.0, 2);
        guestDAO.add(guest);
        roomDAO.add(room);

        Booking booking = new Booking(BOOKING_ID, "2026-11-01", "2026-11-04", guest, room);
        bookingDAO.add(booking);
        Booking found = bookingDAO.findById(BOOKING_ID);
        System.out.println("1. Found: id=" + found.getBookingId()
                + ", in=" + found.getCheckInDate()
                + ", out=" + found.getCheckOutDate()
                + ", status=" + found.getStatus()
                + ", guest=" + found.getGuest().getId()
                + ", room=" + found.getRoom().getRoomNumber());
        System.out.println("   Expected: TESTB1, 2026-11-01, 2026-11-04, Confirmed, TESTG1, 9998");

        // 2. findByGuestId
        List<Booking> history = bookingDAO.findByGuestId(GUEST_ID);
        System.out.println("2. Bookings for " + GUEST_ID + " = " + history.size() + " (expected 1)");

        // 3. updateStatus and read back
        bookingDAO.updateStatus(BOOKING_ID, "Cancelled");
        System.out.println("3. After updateStatus: " + bookingDAO.findById(BOOKING_ID).getStatus()
                + " (expected Cancelled)");

        // 4. findAll includes it
        boolean inAll = false;
        List<Booking> all = bookingDAO.findAll();
        for (Booking b : all) {
            if (BOOKING_ID.equals(b.getBookingId())) {
                inAll = true;
            }
        }
        System.out.println("4. In findAll = " + inAll + " (expected true), total bookings = " + all.size());

        // 5. missing booking returns null
        System.out.println("5. findById(NOBOOKING) = " + bookingDAO.findById("NOBOOKING") + " (expected null)");

        // 6. duplicate add is rejected
        try {
            bookingDAO.add(booking);
            System.out.println("6. FAIL: duplicate was accepted");
        } catch (IllegalStateException ex) {
            System.out.println("6. Duplicate rejected: " + ex.getMessage());
        }

        // 7. booking for a guest that is not in the database is rejected (FK)
        try {
            Guest ghost = new Guest("Ghost", "NOGUEST", "0", "ghost@example.com", 0);
            bookingDAO.add(new Booking("TESTB2", "2026-12-01", "2026-12-02", ghost, room));
            System.out.println("7. FAIL: booking for missing guest accepted");
        } catch (IllegalArgumentException ex) {
            System.out.println("7. Missing guest rejected: " + ex.getMessage());
        }

        // 8. update / delete of a missing booking are rejected
        try {
            bookingDAO.updateStatus("NOBOOKING", "Cancelled");
            System.out.println("8a. FAIL: update of missing booking accepted");
        } catch (IllegalArgumentException ex) {
            System.out.println("8a. Update missing booking rejected: " + ex.getMessage());
        }
        try {
            bookingDAO.delete("NOBOOKING");
            System.out.println("8b. FAIL: delete of missing booking accepted");
        } catch (IllegalArgumentException ex) {
            System.out.println("8b. Delete missing booking rejected: " + ex.getMessage());
        }

        // 9. guest and room that have a booking cannot be deleted (FK)
        try {
            guestDAO.delete(GUEST_ID);
            System.out.println("9a. FAIL: guest with a booking was deleted");
        } catch (IllegalStateException ex) {
            System.out.println("9a. Guest delete blocked: " + ex.getMessage());
        }
        try {
            roomDAO.delete(ROOM_NUMBER);
            System.out.println("9b. FAIL: room with a booking was deleted");
        } catch (IllegalStateException ex) {
            System.out.println("9b. Room delete blocked: " + ex.getMessage());
        }

        // 10. delete the booking, clean up, confirm gone
        bookingDAO.delete(BOOKING_ID);
        cleanUp(bookingDAO, guestDAO, roomDAO);
        System.out.println("10. After delete, findById = " + bookingDAO.findById(BOOKING_ID)
                + ", guest = " + guestDAO.findById(GUEST_ID)
                + ", room = " + roomDAO.findByRoomNumber(ROOM_NUMBER) + " (expected null, null, null)");

        DatabaseManager.getInstance().closeConnection();
    }

    private static void cleanUp(BookingDAO bookingDAO, GuestDAO guestDAO, RoomDAO roomDAO) {
        try {
            bookingDAO.delete(BOOKING_ID);
        } catch (IllegalArgumentException ex) {
            // nothing to remove
        }
        try {
            guestDAO.delete(GUEST_ID);
        } catch (IllegalArgumentException ex) {
            // nothing to remove
        }
        try {
            roomDAO.delete(ROOM_NUMBER);
        } catch (IllegalArgumentException ex) {
            // nothing to remove
        }
    }
}