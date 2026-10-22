package TestPackage;

import com.hotel.hotelbookings.dao.BookingDAO;
import com.hotel.hotelbookings.dao.BookingDAOImpl;
import com.hotel.hotelbookings.dao.DataSeeder;
import com.hotel.hotelbookings.dao.DatabaseManager;
import com.hotel.hotelbookings.dao.GuestDAO;
import com.hotel.hotelbookings.dao.GuestDAOImpl;
import com.hotel.hotelbookings.dao.RoomDAO;
import com.hotel.hotelbookings.dao.RoomDAOImpl;

public class DataSeederTest {

    public static void main(String[] args) {
        RoomDAO roomDAO = new RoomDAOImpl();
        GuestDAO guestDAO = new GuestDAOImpl();
        BookingDAO bookingDAO = new BookingDAOImpl(guestDAO, roomDAO);
        DataSeeder seeder = new DataSeeder(roomDAO, guestDAO, bookingDAO);

        seeder.seedIfEmpty();
        System.out.println("Rooms:    " + roomDAO.findAll().size());
        System.out.println("Guests:   " + guestDAO.findAll().size());
        System.out.println("Bookings: " + bookingDAO.findAll().size());

        seeder.seedIfEmpty();
        System.out.println("After second seed (should be unchanged):");
        System.out.println("Rooms:    " + roomDAO.findAll().size());
        System.out.println("Guests:   " + guestDAO.findAll().size());
        System.out.println("Bookings: " + bookingDAO.findAll().size());

        DatabaseManager.getInstance().closeConnection();
    }
}