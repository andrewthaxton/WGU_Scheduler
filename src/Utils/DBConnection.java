package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    //JDBC URL parts
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//wgudb.ucertify.com:3306/";
    private static final String dbName = "WJ082yt";

    // JDBC URL
    private static final String jdbcURL = protocol + vendorName + ipAddress + dbName;

    // Driver and Connection Interface reference
    private static final String MYSQLJDBCDriver = "com.mysql.jdbc.Driver";
    private static Connection conn = null;

    private static final String username = "U082yt"; // Username
    private static final String password = "53689200369"; // Password

    public static Connection startConnection (){
        try {
            Class.forName(MYSQLJDBCDriver);
            conn = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connection successful!");
        }
        catch(ClassNotFoundException e){
            e.printStackTrace();
            //System.out.println(e.getMessage());
        }
        catch(SQLException e){
            //System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        return conn;
    }

    public static Connection getConnection(){
        return conn;
    }

    public static void closeConnection() {
        try {
            conn.close();
            System.out.println("Connection closed!");
        }
        catch(SQLException e){
            //do nothing
        }
    }
}
