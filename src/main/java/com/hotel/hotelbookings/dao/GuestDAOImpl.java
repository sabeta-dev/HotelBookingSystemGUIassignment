/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.hotelbookings.dao;

import com.hotel.hotelbookings.model.Guest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sashasabeta
 */
public class GuestDAOImpl implements GuestDAO {

    private final Connection conn = DatabaseManager.getInstance().getConnection();

    private Guest mapRow(ResultSet rs) throws SQLException {
        Guest guest = new Guest(rs.getString("NAME"), rs.getString("GUEST_ID"), rs.getString("PHONE"), rs.getString("EMAIL"), rs.getInt("LOYALTY_POINTS"));

        return guest;
    }

    @Override
    public List<Guest> findAll() {
        List<Guest> Allguests = new ArrayList<>();
        String sqlQuery = "SELECT GUEST_ID, NAME, EMAIL, PHONE, LOYALTY_POINTS "
                + "FROM GUESTS ORDER BY NAME";
        try (Statement statement = conn.createStatement(); ResultSet rs = statement.executeQuery(sqlQuery)) {
            while (rs.next()) {
                Allguests.add(mapRow(rs));
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Could not load guests", ex);
        }
        return Allguests;

    }

    @Override
    public Guest findById(String guestId) {
        String sqlQuery = "SELECT GUEST_ID, NAME, EMAIL, PHONE, LOYALTY_POINTS "
                + "FROM GUESTS " + "WHERE GUEST_ID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
            pstmt.setString(1, guestId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Guest could not be found ", ex);
        }
        return null;
    }

    @Override
    public void add(Guest guest) {

        String sqlQuery = "INSERT INTO GUESTS(GUEST_ID, NAME, EMAIL, PHONE, LOYALTY_POINTS) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
            pstmt.setString(1, guest.getId());
            pstmt.setString(2, guest.getName());
            pstmt.setString(3, guest.getEmail());
            pstmt.setString(4, guest.getNumber());
            pstmt.setInt(5, guest.getLoyaltyPoints());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            if ("23505".equals(ex.getSQLState())) {
                throw new IllegalStateException("Guest " + guest.getId() + " already exists.", ex);
            }

            throw new IllegalStateException("Database error: Could not add guest.", ex);
        }
    }

    @Override
    public void update(Guest guest) {
        String sqlQuery = "UPDATE GUESTS  SET NAME = ?, EMAIL = ?, PHONE = ?, LOYALTY_POINTS = ?" + " WHERE GUEST_ID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
            pstmt.setString(1, guest.getName());
            pstmt.setString(2, guest.getEmail());
            pstmt.setString(3, guest.getNumber());
            pstmt.setInt(4, guest.getLoyaltyPoints());
            pstmt.setString(5, guest.getId());
            int affectedRow = pstmt.executeUpdate();
            if (affectedRow == 0) {
                throw new IllegalArgumentException("Guest " + guest.getId() + " does not exist.");
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Database error (could not update guest): " + guest.getId(), ex);
        }
    }

    @Override
    public void delete(String guestId) {
        String sqlQuery = "DELETE FROM GUESTS WHERE GUEST_ID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
            pstmt.setString(1, guestId);
            int affectedRow = pstmt.executeUpdate();
            if (affectedRow == 0) {
                throw new IllegalArgumentException("Guest " + guestId + " does not exist.");
            }
        } catch (SQLException ex) {
            if ("23503".equals(ex.getSQLState())) {
                throw new IllegalStateException("Guest " + guestId + " has bookings and cannot be deleted", ex);
            }
            throw new IllegalStateException("Database error (could not delete guest): " + guestId, ex);
        }

    }
}
