/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.hotelbookings.dao;

import com.hotel.hotelbookings.model.Booking;
import com.hotel.hotelbookings.model.Guest;

import com.hotel.hotelbookings.model.Room;
import com.hotel.hotelbookings.model.RoomFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;

/**
 *
 * @author sashasabeta
 */
public class DataSeeder {

    private final GuestDAO guestDAO;
    private final RoomDAO roomDAO;
    private final BookingDAO bookingDAO;
    private final Connection conn = DatabaseManager.getInstance().getConnection();

    private void loadRooms() {
        InputStream in = getClass().getResourceAsStream("/rooms.txt");
        if (in == null) {
            throw new IllegalStateException("rooms.txt not found on the class path");
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    if (line.trim().isEmpty()) {
                        continue;
                    }
                    String[] part = line.trim().split(",");
                    if (part.length <= 5) {
                        Room room
                                = RoomFactory.createRoom(part[0],
                                        Integer.parseInt(part[1].trim()),
                                        Double.parseDouble(part[2].trim()),
                                        Integer.parseInt(part[3].trim()));
                        room.setIsAvailable(Boolean.parseBoolean(part[4].trim()));
                        roomDAO.add(room);

                    }

                } catch (IllegalArgumentException | IllegalStateException ex) {
                    System.out.println("Skipped a line in rooms.txt (" + ex.getMessage() + ")");
                }
            }
        } catch (IOException ex) {
            System.out.println("Could not add room.");
        }
    }

    private void loadGuests() {
        InputStream in = getClass().getResourceAsStream("/guests.txt");
        if (in == null) {
            throw new IllegalStateException("guests.txt not found on the classpath");
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] part = line.split(",");
                if (part.length < 5) {
                    System.out.println("Skipped line in guests.txt (expected 5 fields): " + line);
                    continue;
                }
                try {
                    Guest guest = new Guest(part[0].trim(), part[1].trim(), part[2].trim(),
                            part[3].trim(), Integer.parseInt(part[4].trim()));
                    guestDAO.add(guest);
                } catch (IllegalArgumentException | IllegalStateException ex) {
                    System.out.println("Skipped line in guests.txt: " + line + " (" + ex.getMessage() + ")");
                }
            }
        } catch (IOException ex) {
            throw new IllegalStateException("Could not read guests.txt", ex);
        }
    }

    private void loadBookings() {
        InputStream in = getClass().getResourceAsStream("/bookings.txt");
        if (in == null) {
            throw new IllegalStateException("bookings.txt not found on the classpath");
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] part = line.split(",");
                if (part.length < 6) {
                    System.out.println("Skipped line in bookings.txt (expected 6 fields): " + line);
                    continue;
                }
                try {
                    Guest guest = guestDAO.findById(part[4].trim());
                    Room room = roomDAO.findByRoomNumber(Integer.parseInt(part[5].trim()));
                    if (guest == null || room == null) {
                        System.out.println("Skipped booking " + part[0].trim() + ": guest or room not found");
                        continue;
                    }
                    Booking booking = new Booking(part[0].trim(), part[1].trim(), part[2].trim(), guest, room);
                    booking.setStatus(part[3].trim());
                    bookingDAO.add(booking);
                } catch (IllegalArgumentException | IllegalStateException ex) {
                    System.out.println("Skipped line in bookings.txt: " + line + " (" + ex.getMessage() + ")");
                }
            }
        } catch (IOException ex) {
            throw new IllegalStateException("Could not read bookings.txt", ex);
        }
    }

    public DataSeeder(RoomDAO roomDAO, GuestDAO guestDAO, BookingDAO bookingDAO) {
        this.roomDAO = roomDAO;
        this.guestDAO = guestDAO;
        this.bookingDAO = bookingDAO;
    }

    public void seedIfEmpty() {

        if (!roomDAO.findAll().isEmpty()) {
            return;
        }

        loadRooms();
        loadGuests();
        loadBookings();
    }

}
