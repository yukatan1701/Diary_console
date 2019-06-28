package diary;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
	private static final String url = "jdbc:mysql://localhost:3306/" + Client.dbname;
    private static final String user = "root";
    private static final String password = "436813";
    private static Connection con;
    private static Statement stmt;
    public static ResultSet rs;
    
    DBConnection() {
    	try {
    		con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
    	} catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }
    
    public void closeConnection() {
    	try { con.close(); } catch (SQLException se) {}
        try { stmt.close(); } catch (SQLException se) {}
        try { rs.close(); } catch (SQLException se) {}
    }
    
	public ResultSet getQueryResult (String query) {
        try {
            rs = stmt.executeQuery(query);
            return rs;
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return null;
	}
}
