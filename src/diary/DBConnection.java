package diary;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
   // private static final String url = "jdbc:derby://localhost:1527/"
   // 		+ Client.dbname + ";";
    private static final String url = "jdbc:derby:" + Client.dbname + ";";
    private static Connection con = null;
    private static Statement stmt = null;
    private static ResultSet rs = null;
    
    DBConnection() {
    	try {
    		Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
    		con = DriverManager.getConnection(url + "create=true");
            stmt = con.createStatement();
            stmt.execute(Query.createTable());
    	} catch (SQLException ex) {
    		if (!DerbyErrors.tableAlreadyExists(ex)) {
    			ex.printStackTrace();
    		}
    	} catch (Exception ex) {
            ex.printStackTrace();
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
    		if (!DerbyErrors.tableAlreadyExists(sqlEx)) {
    			sqlEx.printStackTrace();
    		}
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
    
    public static void shutdown() {
    	try {
    		DriverManager.getConnection("jdbc:derby:" + Client.dbname +
    				";shutdown=true"); 
    	} catch (SQLException ex) {
    		if (!DerbyErrors.shutdownMessage(ex)) {
    			System.out.println(ex.getErrorCode());
    			ex.printStackTrace();
    		}
    	}
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
