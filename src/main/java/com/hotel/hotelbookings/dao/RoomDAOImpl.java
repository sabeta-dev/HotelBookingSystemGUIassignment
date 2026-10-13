/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.hotelbookings.dao;

import com.hotel.hotelbookings.model.Room;
import com.hotel.hotelbookings.model.RoomFactory;
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
public class RoomDAOImpl implements RoomDAO {

    private final Connection conn = DatabaseManager.getInstance().getConnection();

    private Room mapRow(ResultSet rs) throws SQLException {
        Room room = RoomFactory.createRoom(
                rs.getString("ROOMTYPE"),
                rs.getInt("ROOM_NUMBER"),
                rs.getDouble("PRICE"),
                rs.getInt("CAPACITY"));
        room.setIsAvailable(rs.getBoolean("AVAILABLE"));
        return room;
    }

    @Override
    public List<Room> findAll() {
        List<Room> rooms = new ArrayList<>();

        String sqlQuery = "SELECT ROOM_NUMBER, ROOMTYPE, PRICE, CAPACITY, AVAILABLE "
                + "FROM ROOMS ORDER BY ROOM_NUMBER";

        try (Statement statement = conn.createStatement(); ResultSet rs = statement.executeQuery(sqlQuery)) {
            while (rs.next()) {
                rooms.add(mapRow(rs));
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Could not load rooms", ex);
        }
        return rooms;

    }

    @Override
    public List<Room> findAvailable() {
        List<Room> availableRooms = new ArrayList<>();
        String sqlQuery = "SELECT ROOM_NUMBER, ROOMTYPE, PRICE, CAPACITY, AVAILABLE "
                + " FROM ROOMS " + " WHERE AVAILABLE = TRUE " + " ORDER BY ROOM_NUMBER";
        try (Statement statement = conn.createStatement(); ResultSet rs = statement.executeQuery(sqlQuery)) {
            while (rs.next()) {
                availableRooms.add(mapRow(rs));
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Could not load available rooms", ex);
        }
        return availableRooms;
    }

    @Override
    public Room findByRoomNumber(int roomNumber) {
        String sqlQuery = "SELECT ROOM_NUMBER, ROOMTYPE, PRICE, CAPACITY, AVAILABLE " + "FROM ROOMS"
                + " WHERE ROOM_NUMBER = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
            pstmt.setInt(1, roomNumber);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);

                }
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Room could not be found ", ex);
        }
        return null;
    }

    @Override
    public void add(Room room) {
        String sqlQuery = "INSERT INTO ROOMS (ROOM_NUMBER, ROOMTYPE, PRICE, CAPACITY, AVAILABLE) VALUES (?, ?, ?, ?, ?) ";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

            pstmt.setInt(1, room.getRoomNumber());
            pstmt.setString(2, room.getRoomType());
            pstmt.setDouble(3, room.getPricePerNight());
            pstmt.setInt(4, room.getCapacity());
            pstmt.setBoolean(5, room.getIsAvailable());
           pstmt.executeUpdate();

        } catch (SQLException ex) {
            if ("23505".equals(ex.getSQLState()))
            {
                throw new IllegalStateException("Room "+room.getRoomNumber()+" already exists.",ex);
            }
            
            throw new IllegalStateException("Database error: Could not add room.", ex);
        }
    }

    @Override
    public void update(Room room) {
        String sqlQuery = "Update ROOMS SET ROOMTYPE = ?, PRICE = ?, CAPACITY = ?, AVAILABLE = ?" + " WHERE ROOM_NUMBER = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
            pstmt.setString(1, room.getRoomType());
            pstmt.setDouble(2, room.getPricePerNight());
            pstmt.setInt(3, room.getCapacity());
            pstmt.setBoolean(4, room.getIsAvailable());
            pstmt.setInt(5, room.getRoomNumber());
            int affected_row = pstmt.executeUpdate();
            if (affected_row == 0) {
                throw new IllegalArgumentException("Room " + room.getRoomNumber()+" does not exist.");
            }

        } catch (SQLException ex) {
            throw new IllegalStateException("Database operation failure! (could not update room) "+room.getRoomNumber(), ex);
        }

    }

    @Override
    public void delete(int roomNumber) {
        String sqlQuery = "DELETE FROM ROOMS WHERE ROOM_NUMBER = ? ";

        try (PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
            pstmt.setInt(1, roomNumber);
            int affected_row = pstmt.executeUpdate();
            if (affected_row == 0) {
                throw new IllegalArgumentException("Room "+roomNumber+" does not exist ");
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Database operation failure! (Could not delete room) "+roomNumber, ex);
        }
    }
}
