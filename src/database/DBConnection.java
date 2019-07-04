package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
	// For Derby server connection:
    /* private static final String url = "jdbc:derby://localhost:1527/"
    		+ Client.dbname + ";";*/
	public final String dbname;
    private final String url;
    private static Connection con = null;
    private static Statement stmt = null;
    private static ResultSet rs = null;
    
    public DBConnection(String dbname) {
    	this.dbname = dbname;
    	url = "jdbc:derby:" + dbname + ";";
    	try {
    		Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
    		con = DriverManager.getConnection(url + "create=true");
            stmt = con.createStatement();
            stmt.execute(Query.createTable(dbname));
    	} catch (SQLException ex) {
    		if (!DerbyException.tableAlreadyExists(ex)) {
    			ex.printStackTrace();
    		}
    	} catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void insertNote(String query) {
    	try {
    		stmt.executeUpdate(query);
    	} catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void execute(String query) {
    	try {
    		stmt.execute(query);
    	} catch (SQLException ex) {
    		if (!DerbyException.tableAlreadyExists(ex)) {
    			ex.printStackTrace();
    		}
    	}
    }
    
    public void deleteNote(String query) {
    	try {
    		stmt.execute(query);
    	} catch (SQLException ex) {
    		ex.printStackTrace();
        }
    }
    
    public void editNote(String query) {
    	try {
    		stmt.executeUpdate(query);
    	} catch (SQLException ex) {
    		ex.printStackTrace();
    	}
    }
    
    public void closeConnection() {
    	try { if (con != null) con.close(); } catch (SQLException se) {}
        try { if (stmt != null) stmt.close(); } catch (SQLException se) {}
        try { if (rs != null) rs.close(); } catch (SQLException se) {}
        shutdown();
    }
    
    public void shutdown() {
    	try {
    		DriverManager.getConnection("jdbc:derby:" + dbname +
    				";shutdown=true"); 
    	} catch (SQLException ex) {
    		if (!DerbyException.shutdownMessage(ex)) {
    			System.out.println(ex.getErrorCode());
    			ex.printStackTrace();
    		}
    	}
    }
    
	public ResultSet getQueryResult(String query) {
		rs = null;
        try {
            rs = stmt.executeQuery(query);
        } catch (SQLException ex) {
           ex.printStackTrace();
        }
        return rs;
	}
}
