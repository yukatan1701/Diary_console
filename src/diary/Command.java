package diary;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.io.*;
import java.util.Vector;
import java.util.Collections;

interface Command {
	enum COMMAND_TYPE {ADD, ALL, DELETE, EXIT, HELP, SHOW};
	void execute(DBConnection db, Scanner in) throws Exception;
	public COMMAND_TYPE getType();
}

class All implements Command {
	All(String[] words) throws CommandException {
		if (words.length > 1)
			throw new CommandException(CommandException.ID.MANY_ARGS);
	}
	
	public void execute(DBConnection db, Scanner in) {
		ResultSet rs = db.getQueryResult("SELECT * FROM " + Client.dbname);
		Terminal.printHeader();
		try {
			while (rs.next()) {
		        int id = rs.getInt("ID");
		        String date = rs.getString("DATE");
		    	String title = rs.getString("TITLE");
		        String text = rs.getString("TEXT");
		        Terminal.printTableRow(id, date, title, text);
			}
		} catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
	}
	public COMMAND_TYPE getType() {
		return COMMAND_TYPE.ALL;
	}
}

// test class
class Create implements Command {
	Create (String[] words) throws CommandException {
		
	}
	
	public void execute(DBConnection db, Scanner in) {
		db.execute("create table diary (id int not null primary key "
				+ "generated always as identity, date DATE, "
				+ "title VARCHAR(128), text VARCHAR(4096))");
	}
	public COMMAND_TYPE getType() {
		return COMMAND_TYPE.ALL;
	}
}

class Show implements Command {
	private int id;
	
	Show(String[] words) throws CommandException {
		if (words.length < 2)
			throw new CommandException(CommandException.ID.FEW_ARGS);
		else if (words.length > 2)
			throw new CommandException(CommandException.ID.MANY_ARGS);
		id = Integer.parseInt(words[1]);
	}
	
	public void execute(DBConnection db, Scanner in) {
		ResultSet rs = db.getQueryResult("select * from " + Client.dbname + " where id = " + id);
		try {
			if (rs.next()) {
				String date = rs.getString("DATE");
		    	String title = rs.getString("TITLE");
		        String text = rs.getString("TEXT");
		        Terminal.printNote(id, date, title, text);
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
	int begin = -1;
	int end = -1;
	Delete(String[] words) throws CommandException {
		if (words.length < 2)
			throw new CommandException(CommandException.ID.FEW_ARGS);
		if (words.length >= 2)
			begin = Integer.parseInt(words[1]);
		if (words.length == 3)
			end = Integer.parseInt(words[2]);
	}
	
	public void execute(DBConnection db, Scanner in) {
		String base = "delete from " + Client.dbname + " where id = ";
		int to = begin;
		if (end != -1)
			to = end;
		for (int from = begin; from <= to; from++) {
			db.deleteNote(base.concat(String.valueOf(from)));
		}
	}
	
	public COMMAND_TYPE getType() {
		return COMMAND_TYPE.DELETE;
	}
}

class Add implements Command {
	Add(String[] words) throws CommandException {
		if (words.length > 1)
			throw new CommandException(CommandException.ID.MANY_ARGS);
	}
	
	public void execute(DBConnection db, Scanner in) {
		System.out.println("Title:");
		String title = in.nextLine();
		System.out.println("Text:");
		String text = in.nextLine();
		String query = String.format("insert into %s values (default, "
				+ "CURRENT_DATE, \'%s\',\'%s\')", Client.dbname, title, text);
		db.insertNote(query);
	}
	
	public COMMAND_TYPE getType() {
		return COMMAND_TYPE.ADD;
	}
}

class Help implements Command {
	Help(String[] words) throws CommandException {
		if (words.length > 1)
			throw new CommandException(CommandException.ID.MANY_ARGS);
	}
	
	public void execute(DBConnection db, Scanner in) throws Exception {
		FileReader file = new FileReader("help.txt");
		Scanner scan = new Scanner(file);
		Vector<String> commands = new Vector<String>();
		while (scan.hasNextLine()) {
			commands.add(scan.nextLine());
		}
		Collections.sort(commands);
		for (String line: commands) {
			System.out.println(line);
		}
		scan.close();
	}
	
	public COMMAND_TYPE getType() {
		return COMMAND_TYPE.HELP;
	}
}

class Exit implements Command {
	Exit(String[] words) throws CommandException {
		if (words.length > 1)
			throw new CommandException(CommandException.ID.MANY_ARGS);
	}
	
	public void execute(DBConnection db, Scanner in) {
		
	}
	
	public COMMAND_TYPE getType() {
		return COMMAND_TYPE.EXIT;
	}
}