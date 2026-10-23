/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.hotel.hotelbookings;

import com.hotel.hotelbookings.controller.HotelController;
import com.hotel.hotelbookings.dao.BookingDAO;
import com.hotel.hotelbookings.dao.BookingDAOImpl;
import com.hotel.hotelbookings.dao.DataSeeder;
import com.hotel.hotelbookings.dao.GuestDAO;
import com.hotel.hotelbookings.dao.GuestDAOImpl;
import com.hotel.hotelbookings.dao.RoomDAO;
import com.hotel.hotelbookings.dao.RoomDAOImpl;
import com.hotel.hotelbookings.service.HotelService;
import com.hotel.hotelbookings.service.HotelServiceImpl;
import com.hotel.hotelbookings.view.MainFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author sashasabeta
 */
public class HotelBookingSystemGUIassignment {

    public static void main(String[] args) {
        RoomDAO roomDAO = new RoomDAOImpl();
        GuestDAO guestDAO = new GuestDAOImpl();
        BookingDAO bookingDAO = new BookingDAOImpl(guestDAO, roomDAO);
        new DataSeeder(roomDAO, guestDAO, bookingDAO).seedIfEmpty();

        HotelService service = new HotelServiceImpl(roomDAO, guestDAO, bookingDAO);
        HotelController controller = new HotelController(service, roomDAO, guestDAO, bookingDAO);

        SwingUtilities.invokeLater(() -> new MainFrame(controller).setVisible(true));

    }
}
