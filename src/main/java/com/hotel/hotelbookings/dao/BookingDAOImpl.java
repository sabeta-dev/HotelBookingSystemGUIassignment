package com.hotel.hotelbookings.dao;

import com.hotel.hotelbookings.model.Booking;
import com.hotel.hotelbookings.model.Guest;
import com.hotel.hotelbookings.model.Room;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BookingDAOImpl implements BookingDAO {

    private static final String SELECT_COLUMNS
            = "SELECT BOOKING_ID, START_DATE, END_DATE, BOOKING_STATUS, GUEST_ID, ROOM_NUMBER FROM BOOKINGS ";

    private final Connection conn = DatabaseManager.getInstance().getConnection();
    private final GuestDAO guestDAO;
    private final RoomDAO roomDAO;

    public BookingDAOImpl(GuestDAO guestDAO, RoomDAO roomDAO) {
        this.guestDAO = guestDAO;
        this.roomDAO = roomDAO;
    }

    private Booking mapRow(ResultSet rs) throws SQLException {
        String guestId = rs.getString("GUEST_ID");
        int roomNumber = rs.getInt("ROOM_NUMBER");

        Guest guest = guestDAO.findById(guestId);
        Room room = roomDAO.findByRoomNumber(roomNumber);
        if (guest == null || room == null) {
            throw new IllegalStateException("Booking " + rs.getString("BOOKING_ID")
                    + " refers to a missing guest or room");
        }

        Booking booking = new Booking(
                rs.getString("BOOKING_ID"),
                rs.getString("START_DATE"),
                rs.getString("END_DATE"),
                guest,
                room);
        booking.setStatus(rs.getString("BOOKING_STATUS"));
        return booking;
    }

    @Override
    public List<Booking> findAll() {
        List<Booking> bookings = new ArrayList<>();
        String sqlQuery = SELECT_COLUMNS + "ORDER BY BOOKING_ID";
        try (Statement statement = conn.createStatement(); ResultSet rs = statement.executeQuery(sqlQuery)) {
            while (rs.next()) {
                bookings.add(mapRow(rs));
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Could not load bookings", ex);
        }
        return bookings;
    }

    @Override
    public Booking findById(String bookingId) {
        String sqlQuery = SELECT_COLUMNS + "WHERE BOOKING_ID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
            pstmt.setString(1, bookingId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Could not load booking " + bookingId, ex);
        }
        return null;
    }

    @Override
    public List<Booking> findByGuestId(String guestId) {
        List<Booking> bookings = new ArrayList<>();
        String sqlQuery = SELECT_COLUMNS + "WHERE GUEST_ID = ? ORDER BY BOOKING_ID";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
            pstmt.setString(1, guestId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapRow(rs));
                }
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Could not load bookings for guest " + guestId, ex);
        }
        return bookings;
    }

    @Override
    public void add(Booking booking) {
        String sqlQuery = "INSERT INTO BOOKINGS (BOOKING_ID, START_DATE, END_DATE, BOOKING_STATUS, GUEST_ID, ROOM_NUMBER) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
            pstmt.setString(1, booking.getBookingId());
            pstmt.setString(2, booking.getCheckInDate());
            pstmt.setString(3, booking.getCheckOutDate());
            pstmt.setString(4, booking.getStatus());
            pstmt.setString(5, booking.getGuest().getId());
            pstmt.setInt(6, booking.getRoom().getRoomNumber());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            if ("23505".equals(ex.getSQLState())) {
                throw new IllegalStateException("Booking " + booking.getBookingId() + " already exists.", ex);
            }
            if ("23503".equals(ex.getSQLState())) {
                throw new IllegalArgumentException("Booking " + booking.getBookingId()
                        + " refers to a guest or room that does not exist.", ex);
            }
            throw new IllegalStateException("Database error (could not add booking): "
                    + booking.getBookingId(), ex);
        }
    }

    @Override
    public void updateStatus(String bookingId, String status) {
        String sqlQuery = "UPDATE BOOKINGS SET BOOKING_STATUS = ? WHERE BOOKING_ID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
            pstmt.setString(1, status);
            pstmt.setString(2, bookingId);
            int affectedRow = pstmt.executeUpdate();
            if (affectedRow == 0) {
                throw new IllegalArgumentException("Booking " + bookingId + " does not exist.");
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Database error (could not update booking): " + bookingId, ex);
        }
    }

    @Override
    public void delete(String bookingId) {
        String sqlQuery = "DELETE FROM BOOKINGS WHERE BOOKING_ID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
            pstmt.setString(1, bookingId);
            int affectedRow = pstmt.executeUpdate();
            if (affectedRow == 0) {
                throw new IllegalArgumentException("Booking " + bookingId + " does not exist.");
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Database error (could not delete booking): " + bookingId, ex);
        }
    }

    @Override
    public void updateDates(String bookingId, String checkIn, String checkOut) {
        String sqlQuery = "UPDATE BOOKINGS SET START_DATE = ?, END_DATE = ? WHERE BOOKING_ID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
            pstmt.setString(1, checkIn);
            pstmt.setString(2, checkOut);
            pstmt.setString(3, bookingId);
            int affectedRow = pstmt.executeUpdate();
            if (affectedRow == 0) {
                throw new IllegalArgumentException("Booking " + bookingId + " does not exist.");
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Database error (could not update dates): " + bookingId, ex);
        }
    }
}
