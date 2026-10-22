/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.hotelbookings.model;

/**
 *
 * @author sashasabeta
 */
import com.hotel.hotelbookings.dao.BookingDAO;
import com.hotel.hotelbookings.dao.BookingDAOImpl;
import com.hotel.hotelbookings.dao.GuestDAO;
import com.hotel.hotelbookings.dao.GuestDAOImpl;
import com.hotel.hotelbookings.dao.RoomDAO;
import com.hotel.hotelbookings.dao.RoomDAOImpl;
import com.hotel.hotelbookings.exception.BookingException;
import com.hotel.hotelbookings.exception.ValidationException;
import com.hotel.hotelbookings.model.Booking;
import com.hotel.hotelbookings.model.Guest;
import com.hotel.hotelbookings.model.RoomFactory;
import com.hotel.hotelbookings.service.HotelService;
import com.hotel.hotelbookings.service.HotelServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class HotelServiceTest {

    private static final String GUEST_ID = "TESTG1";
    private static final int ROOM_NUMBER = 9997;

    private RoomDAO roomDAO;
    private GuestDAO guestDAO;
    private BookingDAO bookingDAO;
    private HotelService service;

    @Before
    public void setUp() {
        roomDAO = new RoomDAOImpl();
        guestDAO = new GuestDAOImpl();
        bookingDAO = new BookingDAOImpl(guestDAO, roomDAO);
        service = new HotelServiceImpl(roomDAO, guestDAO, bookingDAO);
        removeTestData();
        guestDAO.add(new Guest("Test Guest", GUEST_ID, "0211234567", "test@example.com", 0));
        roomDAO.add(RoomFactory.createRoom("Standard", ROOM_NUMBER, 100.0, 2));
    }

    @After
    public void tearDown() {
        removeTestData();
    }

    private void removeTestData() {
        for (Booking b : bookingDAO.findByGuestId(GUEST_ID)) {
            bookingDAO.delete(b.getBookingId());
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

    @Test
    public void calculateNightsCountsDaysBetweenDates() {
        assertEquals(3, service.calculateNights("2026-11-01", "2026-11-04"));
    }

    @Test(expected = ValidationException.class)
    public void checkOutBeforeCheckInIsRejected() {
        service.calculateNights("2026-11-04", "2026-11-01");
    }

    @Test(expected = ValidationException.class)
    public void badDateFormatIsRejected() {
        service.calculateNights("01/11/2026", "04/11/2026");
    }

    @Test(expected = ValidationException.class)
    public void guestWithInvalidEmailIsRejected() {
        service.addGuest("TESTG2", "Someone", "021999", "not-an-email");
    }

    @Test(expected = ValidationException.class)
    public void guestWithEmptyNameIsRejected() {
        service.addGuest("TESTG2", "  ", "021999", "a@b.com");
    }

    @Test
    public void createBookingMarksRoomUnavailableAndCostsCorrectly() {
        Booking booking = service.createBooking(GUEST_ID, ROOM_NUMBER, "2026-11-01", "2026-11-04");
        assertFalse(roomDAO.findByRoomNumber(ROOM_NUMBER).getIsAvailable());
        assertEquals(300.0, service.calculateBookingCost(booking), 0.01);
    }

    @Test(expected = BookingException.class)
    public void bookingAnUnavailableRoomIsRejected() {
        service.createBooking(GUEST_ID, ROOM_NUMBER, "2026-11-01", "2026-11-04");
        service.createBooking(GUEST_ID, ROOM_NUMBER, "2026-12-01", "2026-12-03");
    }

    @Test(expected = BookingException.class)
    public void bookingForUnknownGuestIsRejected() {
        service.createBooking("NOGUEST", ROOM_NUMBER, "2026-11-01", "2026-11-04");
    }

    @Test
    public void cancellingABookingFreesTheRoom() {
        Booking booking = service.createBooking(GUEST_ID, ROOM_NUMBER, "2026-11-01", "2026-11-04");
        service.cancelBooking(booking.getBookingId());
        assertTrue(roomDAO.findByRoomNumber(ROOM_NUMBER).getIsAvailable());
        assertEquals("Cancelled", bookingDAO.findById(booking.getBookingId()).getStatus());
    }
}