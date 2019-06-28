package diary;

import java.sql.ResultSet;
import java.sql.SQLException;

interface Command {
	enum COMMAND_TYPE {ADD, ALL, DELETE, EXIT, HELP, SHOW};
	enum ARGUMENT_ERROR {MANY, FEW};
	static void printError(ARGUMENT_ERROR err) {
		if (err == ARGUMENT_ERROR.MANY) {
			System.out.println("Too many arguments. Extra commands will be ignored.");
		} else if (err == ARGUMENT_ERROR.FEW) {
			System.out.println("Too few arguments. Terminate.");
		}
	}
	static void printHeader() {
		System.out.printf("%-4s | %-10s | %-12s | %-16s\n", "ID", "DATE", "TITLE", "TEXT");
		System.out.printf("-----+----------+--------------+-----------------\n");
	}
	void execute(DBConnection db);
	public COMMAND_TYPE getType();
}

class All implements Command {
	All(String[] words) {
		if (words.length > 1)
			Command.printError(ARGUMENT_ERROR.MANY);
	}
	public void execute(DBConnection db) {
		ResultSet rs = db.getQueryResult("select * from " + Client.dbname);
		Command.printHeader();
		try {
			while (rs.next()) {
		        int id = rs.getInt("ID");
		        String date = rs.getString("DATE");
		    	String title = rs.getString("TITLE");
		        String text = rs.getString("TEXT");
		        System.out.printf("%-4d | %-10s | %-12s | %-16s\n", id, date, title, text);
			}
		} catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
	}
	public COMMAND_TYPE getType() {
		return COMMAND_TYPE.ALL;
	}
}

class Show implements Command {
	private int id;
	
	Show(String[] words) {
		if (words.length < 2) {
			Command.printError(ARGUMENT_ERROR.FEW);
			id = -1;
		}
		else if (words.length > 2) {
			Command.printError(ARGUMENT_ERROR.MANY);
		}
		id = Integer.parseInt(words[1]);
	}
	
	public void execute(DBConnection db) {
		ResultSet rs = db.getQueryResult("select * from " + Client.dbname + " where id = " + id);
		try {
			if (rs.next()) {
				Command.printHeader();
				String date = rs.getString("DATE");
		    	String title = rs.getString("TITLE");
		        String text = rs.getString("TEXT");
		        System.out.printf("%-4d | %-10s | %-12s | %-16s\n", id, date, title, text);
			}
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		}
	}
	
	public COMMAND_TYPE getType() {
		return COMMAND_TYPE.SHOW;
	}
}

class Delete implements Command {
	Delete(String[] words) {
		if (words.length < 2)
			Command.printError(ARGUMENT_ERROR.FEW);
		else if (words.length > 2)
			Command.printError(ARGUMENT_ERROR.MANY);
	}
	
	public void execute(DBConnection db) {
		
	}
	public COMMAND_TYPE getType() {
		return COMMAND_TYPE.DELETE;
	}
}

class Add implements Command {
	Add(String[] words) {
		if (words.length > 1)
			Command.printError(ARGUMENT_ERROR.MANY);
	}
	
	public void execute(DBConnection db) {
		
	}
	public COMMAND_TYPE getType() {
		return COMMAND_TYPE.ADD;
	}
}

class Help implements Command {
	Help(String[] words) {
		if (words.length > 1)
			Command.printError(ARGUMENT_ERROR.MANY);
	}
	
	public void execute(DBConnection db) {
		
	}
	public COMMAND_TYPE getType() {
		return COMMAND_TYPE.HELP;
	}
}

class Exit implements Command {
	public void execute(DBConnection db) {
		
	}
	public COMMAND_TYPE getType() {
		return COMMAND_TYPE.EXIT;
	}
}