package TestPackage;

import com.hotel.hotelbookings.dao.DatabaseManager;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManagerTest {

    public static void main(String[] args) throws SQLException {
        DatabaseManager db = DatabaseManager.getInstance();
        Connection conn = db.getConnection();

        System.out.println("ROOMS exists: " + db.tableExists("ROOMS"));
        System.out.println("BOOKINGS exists: " + db.tableExists("BOOKINGS"));
        System.out.println("GUESTS exists: " + db.tableExists("GUESTS"));

        
        DatabaseMetaData meta = conn.getMetaData();
        try (ResultSet cols = meta.getColumns(null, null, "GUESTS", null)) {
            while (cols.next()) {
                System.out.println("GUESTS column: " + cols.getString("COLUMN_NAME")
                        + " " + cols.getString("TYPE_NAME")
                        + " nullable=" + cols.getString("IS_NULLABLE"));
            }
        }

        
        deleteTestGuest(conn);
        try (PreparedStatement insert = conn.prepareStatement(
                "INSERT INTO GUESTS (GUEST_ID, NAME, EMAIL, PHONE, LOYALTY_POINTS) "
                + "VALUES (?, ?, ?, ?, ?)")) {
            insert.setString(1, "TEST1");
            insert.setString(2, "Test Guest");
            insert.setString(3, "test@example.com");
            insert.setString(4, "021000000");
            insert.setInt(5, 50);
            insert.executeUpdate();
        }
        try (PreparedStatement select = conn.prepareStatement(
                "SELECT NAME, LOYALTY_POINTS FROM GUESTS WHERE GUEST_ID = ?")) {
            select.setString(1, "TEST1");
            try (ResultSet rs = select.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Read back: " + rs.getString("NAME")
                            + ", points = " + rs.getInt("LOYALTY_POINTS"));
                } else {
                    System.out.println("Read back FAILED: guest not found");
                }
            }
        }

      
        try (PreparedStatement bad = conn.prepareStatement(
                "INSERT INTO BOOKINGS (BOOKING_ID, START_DATE, END_DATE, BOOKING_STATUS, "
                + "GUEST_ID, ROOM_NUMBER) VALUES (?, ?, ?, ?, ?, ?)")) {
            bad.setString(1, "BADBK");
            bad.setString(2, "2026-10-20");
            bad.setString(3, "2026-10-23");
            bad.setString(4, "Confirmed");
            bad.setString(5, "NOGUEST");
            bad.setInt(6, 999);
            bad.executeUpdate();
            System.out.println("FK test FAILED: bad booking was accepted");
        } catch (SQLException ex) {
            System.out.println("FK test passed: " + ex.getMessage());
        }

        deleteTestGuest(conn);
        db.closeConnection();
    
    
    }

    private static void deleteTestGuest(Connection conn) throws SQLException {
        try (PreparedStatement delete = conn.prepareStatement(
                "DELETE FROM GUESTS WHERE GUEST_ID = ?")) {
            delete.setString(1, "TEST1");
            delete.executeUpdate();
        }
    }
    
}
