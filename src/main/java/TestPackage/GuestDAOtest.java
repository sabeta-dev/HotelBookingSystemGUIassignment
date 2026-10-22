/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TestPackage;


import com.hotel.hotelbookings.dao.DatabaseManager;
import com.hotel.hotelbookings.dao.GuestDAO;
import com.hotel.hotelbookings.dao.GuestDAOImpl;
import com.hotel.hotelbookings.model.Guest;
import java.util.List;

/**
 *
 * @author sashasabeta
 */
public class GuestDAOtest {
      private static final String TEST_ID = "TEST1";

    public static void main(String[] args) {
        GuestDAO dao = new GuestDAOImpl();

        // 0. Clean up leftovers from a previous run
        try {
            dao.delete(TEST_ID);
        } catch (IllegalArgumentException ex) {
            System.out.println("0. Nothing to clean up (expected on a fresh run)");
        }

        // 1. add + findById, check every field
        // constructor order: name, id, phone, email, loyaltyPoints
        Guest guest = new Guest("Test Guest", TEST_ID, "0211234567", "test@example.com", 10);
        dao.add(guest);
        Guest found = dao.findById(TEST_ID);
        System.out.println("1. Found: name=" + found.getName()
                + ", id=" + found.getId()
                + ", phone=" + found.getNumber()
                + ", email=" + found.getEmail()
                + ", points=" + found.getLoyaltyPoints());
        System.out.println("   Expected: Test Guest, TEST1, 0211234567, test@example.com, 10");

        // 2. update and read back
        Guest changed = new Guest("Test Guest", TEST_ID, "0217654321", "new@example.com", 50);
        dao.update(changed);
        Guest updated = dao.findById(TEST_ID);
        System.out.println("2. After update: phone=" + updated.getNumber()
                + ", email=" + updated.getEmail()
                + ", points=" + updated.getLoyaltyPoints()
                + " (expected 0217654321, new@example.com, 50)");

        // 3. findAll includes it
        List<Guest> all = dao.findAll();
        boolean inAll = false;
        for (Guest g : all) {
            if (TEST_ID.equals(g.getId())) {
                inAll = true;
            }
        }
        System.out.println("3. In findAll = " + inAll + " (expected true), total guests = " + all.size());

        // 4. missing guest returns null
        System.out.println("4. findById(NOGUEST) = " + dao.findById("NOGUEST") + " (expected null)");

        // 5. duplicate add is rejected
        try {
            dao.add(guest);
            System.out.println("5. FAIL: duplicate was accepted");
        } catch (IllegalStateException ex) {
            System.out.println("5. Duplicate rejected: " + ex.getMessage());
        }

        // 6. update / delete of a missing guest are rejected
        try {
            dao.update(new Guest("Nobody", "NOGUEST", "0", "x@y.z", 0));
            System.out.println("6a. FAIL: update of missing guest accepted");
        } catch (IllegalArgumentException ex) {
            System.out.println("6a. Update missing guest rejected: " + ex.getMessage());
        }
        try {
            dao.delete("NOGUEST");
            System.out.println("6b. FAIL: delete of missing guest accepted");
        } catch (IllegalArgumentException ex) {
            System.out.println("6b. Delete missing guest rejected: " + ex.getMessage());
        }

        // 7. delete and confirm gone
        dao.delete(TEST_ID);
        System.out.println("7. After delete, findById = " + dao.findById(TEST_ID) + " (expected null)");

        DatabaseManager.getInstance().closeConnection();
    }
}

