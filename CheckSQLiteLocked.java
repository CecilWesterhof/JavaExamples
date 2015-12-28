import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;


public class CheckSQLiteLocked {
    // We use this class as a program,
    // so we make sure the class cannot be initiated
    private CheckSQLiteLocked() {
    }

    // Check if the database is locked.
    // If it is give message and return 1
    // On other error give message about the error and return 2
    // Otherwise no message and return 0
    public static void main(String [] args) throws Exception {
        Connection   conn;
        String       database;
        Statement    stmt;

        if (args.length != 1) {
            System.err.println("ERROR: " +
                               getClassName() + " <SQLITE_DATABASE>");
            System.exit(1);
        }
        database = args[0];
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:" + database);
        stmt  = conn.createStatement();
        try {
            stmt.executeUpdate("BEGIN IMMEDIATE");
        } catch (SQLException e) {
            // In a stand-alone program the next two statements are not necessary,
            // but I prefer well written code, so I use them
            stmt.close();
            conn.close();
            // At the moment getErrorCode does not work
            // So getMessage is used
            if (e.getMessage().equals("database is locked")) {
                System.out.printf("%s: the database is locked.\n",
                                  getClassName());
                System.exit(1);
            } else {
                System.err.printf("%s: unexpected error: #%s#\n",
                                  getClassName(), e.getMessage());
                System.exit(2);
            }
        }
        // In a stand-alone program these statements are not necessary,
        // but I prefer well written code, so I use them
        stmt.executeUpdate("ROLLBACK");
        stmt.close();
        conn.close();
    }

    // I need the class name for error messages
    private static String getClassName() {
        return new CheckSQLiteLocked().getClass().getName();
    }

}
