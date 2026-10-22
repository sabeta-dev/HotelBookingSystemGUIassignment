package com.hotel.hotelbookings.model;

import com.hotel.hotelbookings.model.Booking;
import com.hotel.hotelbookings.model.Booking;
import com.hotel.hotelbookings.model.DeluxeRoom;
import com.hotel.hotelbookings.model.DeluxeRoom;
import com.hotel.hotelbookings.model.Guest;
import com.hotel.hotelbookings.model.Guest;
import com.hotel.hotelbookings.model.Room;
import com.hotel.hotelbookings.model.Room;
import com.hotel.hotelbookings.model.RoomFactory;
import com.hotel.hotelbookings.model.RoomFactory;
import com.hotel.hotelbookings.model.StandardRoom;
import com.hotel.hotelbookings.model.StandardRoom;
import com.hotel.hotelbookings.model.Suite;
import com.hotel.hotelbookings.model.Suite;

public class ModelTest {

    public static void main(String[] args) {
        Room standard = new StandardRoom(101, 100.0, 2);
        Room deluxe = new DeluxeRoom(201, 100.0, 3);
        Room suite = new Suite(301, 100.0, 4);

        Room[] rooms = {standard, deluxe, suite};
        for (Room room : rooms) {
            System.out.println(room.getRoomType() + ": " + room.getRoomDetails());
            System.out.println("  3 nights = $" + room.calculatePrice(3));
        }

        standard.setIsAvailable(false);
        System.out.println("Standard available after setIsAvailable(false): "
                + standard.getIsAvailable());
        standard.setIsAvailable(true);
        System.out.println("Standard available after setIsAvailable(true): "
                + standard.getIsAvailable());

        Guest guest = new Guest("Test Guest", "G001", "021000000", "test@example.com", 50);
        Booking booking = new Booking("B001", "2026-10-20", "2026-10-23", guest, deluxe);
        guest.addBooking(booking);

        System.out.println("Guest: " + guest.getName() + " (" + guest.getRole() + "), points: "
                + guest.getLoyaltyPoints());
        System.out.println("Booking " + booking.getBookingId() + " status: " + booking.getStatus());
        System.out.println("Booking total for 3 nights: $" + booking.calculateTotalCost(3));
        System.out.println("History size: " + guest.getBookingHistory().size());
        // RoomFactory tests
        System.out.println("--- RoomFactory ---");
        String[] types = {"Standard", "Deluxe", "Suite", "deluxe", "SUITE"};
        for (String type : types) {
            Room room = RoomFactory.createRoom(type, 400, 100.0, 2);
            System.out.println("Asked for \"" + type + "\" -> got "
                    + room.getClass().getSimpleName()
                    + " (getRoomType: " + room.getRoomType() + ")");
        }

        try {
            RoomFactory.createRoom("Penthouse", 401, 100.0, 2);
            System.out.println("FAILED: Penthouse was accepted");
        } catch (IllegalArgumentException e) {
            System.out.println("Penthouse rejected: " + e.getMessage());
        }

        try {
            RoomFactory.createRoom(null, 402, 100.0, 2);
            System.out.println("FAILED: null was accepted");
        } catch (IllegalArgumentException e) {
            System.out.println("Null rejected: " + e.getMessage());
        }
    }
}
