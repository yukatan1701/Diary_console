package diary;

import java.sql.SQLException;

public final class DerbyException {
	public static boolean tableAlreadyExists(SQLException ex) {
		if (ex.getErrorCode() == 30000)
			return true;
		return false;
	}
	
	public static boolean shutdownMessage(SQLException ex) {
		if (ex.getErrorCode() == 45000 || ex.getErrorCode() == 40000)
			return true;
		return false;
	}
}
