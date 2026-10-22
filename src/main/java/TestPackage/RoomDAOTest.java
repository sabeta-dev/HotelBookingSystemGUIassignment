package TestPackage;

import com.hotel.hotelbookings.dao.DatabaseManager;
import com.hotel.hotelbookings.dao.RoomDAO;
import com.hotel.hotelbookings.dao.RoomDAOImpl;
import com.hotel.hotelbookings.model.Room;
import com.hotel.hotelbookings.model.RoomFactory;
import java.util.List;

public class RoomDAOTest {

    private static final int TEST_ROOM = 9999;

    public static void main(String[] args) {
        RoomDAO dao = new RoomDAOImpl();

        // 0. Clean up leftovers from a previous run
        try {
            dao.delete(TEST_ROOM);
        } catch (IllegalArgumentException ex) {
            System.out.println("0. Nothing to clean up (expected on a fresh run)");
        }

        // 1. add + findByRoomNumber
        Room room = RoomFactory.createRoom("Deluxe", TEST_ROOM, 100.0, 2);
        dao.add(room);
        Room found = dao.findByRoomNumber(TEST_ROOM);
        System.out.println("1. Added and found: " + found.getRoomDetails());
        System.out.println("   Type = " + found.getRoomType()
                + ", price = " + found.getPricePerNight()
                + ", available = " + found.getIsAvailable());

        // 2. update availability and read back
        found.setIsAvailable(false);
        dao.update(found);
        Room updated = dao.findByRoomNumber(TEST_ROOM);
        System.out.println("2. After update, available = " + updated.getIsAvailable()
                + " (expected false)");

        // 3. findAvailable excludes it, findAll includes it
        boolean inAvailable = false;
        for (Room r : dao.findAvailable()) {
            if (r.getRoomNumber() == TEST_ROOM) {
                inAvailable = true;
            }
        }
        boolean inAll = false;
        List<Room> all = dao.findAll();
        for (Room r : all) {
            if (r.getRoomNumber() == TEST_ROOM) {
                inAll = true;
            }
        }
        System.out.println("3. In findAvailable = " + inAvailable + " (expected false)");
        System.out.println("   In findAll = " + inAll + " (expected true), total rooms = " + all.size());

        // 4. missing room returns null
        System.out.println("4. findByRoomNumber(123456) = " + dao.findByRoomNumber(123456)
                + " (expected null)");

        // 5. duplicate add is rejected
        try {
            dao.add(RoomFactory.createRoom("Standard", TEST_ROOM, 80.0, 2));
            System.out.println("5. FAIL: duplicate was accepted");
        } catch (IllegalStateException ex) {
            System.out.println("5. Duplicate rejected: " + ex.getMessage());
        }

        // 6. update / delete of a missing room are rejected
        try {
            dao.update(RoomFactory.createRoom("Standard", 123456, 80.0, 2));
            System.out.println("6a. FAIL: update of missing room accepted");
        } catch (IllegalArgumentException ex) {
            System.out.println("6a. Update missing room rejected: " + ex.getMessage());
        }
        try {
            dao.delete(123456);
            System.out.println("6b. FAIL: delete of missing room accepted");
        } catch (IllegalArgumentException ex) {
            System.out.println("6b. Delete missing room rejected: " + ex.getMessage());
        }

        // 7. delete and confirm gone
        dao.delete(TEST_ROOM);
        System.out.println("7. After delete, findByRoomNumber = " + dao.findByRoomNumber(TEST_ROOM)
                + " (expected null)");

        DatabaseManager.getInstance().closeConnection();
    }
}