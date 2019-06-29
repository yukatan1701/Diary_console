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
    private static Connection con = null;
    private static Statement stmt = null;
    private static ResultSet rs = null;
    
    DBConnection() {
    	try {
    		con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
    	} catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }
    
    public void insertNote(String query) {
    	try {
    		stmt.executeUpdate(query);
    	} catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }
    
    public void execute(String query) {
    	try {
    		stmt.execute(query);
    	} catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }
    
    public void deleteNote(String query) {
    	try {
    		stmt.execute(query);
    	} catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }
    
    public void closeConnection() {
    	try { if (con != null) con.close(); } catch (SQLException se) {}
        try { if (stmt != null) stmt.close(); } catch (SQLException se) {}
        try { if (rs != null) rs.close(); } catch (SQLException se) {}
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
