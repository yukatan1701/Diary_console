package diary;

import java.sql.ResultSet;
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
		try {
			ResultSet rs = db.getQueryResult(Query.selectAll());
			Terminal.printHeader();
			while (rs.next()) {
		        int id = rs.getInt("ID");
		        String date = rs.getString("DATE");
		    	String title = rs.getString("TITLE");
		        String text = rs.getString("TEXT");
		        Terminal.printTableRow(id, date, title, text);
			}
		} catch (Exception ex) {
            ex.printStackTrace();
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
		try {
			db.execute(Query.createTable());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
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
		try {
			ResultSet rs = db.getQueryResult(Query.selectById(id));
			if (rs.next()) {
				String date = rs.getString("DATE");
		    	String title = rs.getString("TITLE");
		        String text = rs.getString("TEXT");
		        Terminal.printNote(id, date, title, text);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public COMMAND_TYPE getType() {
		return COMMAND_TYPE.SHOW;
	}
}

class Delete implements Command {
	int[] rows;
	private enum DELETE_TYPE { BETWEEN, INDIVIDUALLY };
	DELETE_TYPE type;
	
	Delete(String[] words) throws CommandException {
		if (words.length < 2)
			throw new CommandException(CommandException.ID.FEW_ARGS);
		if (words.length == 2)
			rows = getRowsByExpression(words[1]);
		if (words.length > 2)
			throw new CommandException(CommandException.ID.MANY_ARGS);
	}
	
	private int[] getRowsByExpression(String expr) throws CommandException {
		boolean has_dash = expr.contains("-");
		boolean has_comma = expr.contains(",");
		if (has_dash && has_comma)
			throw new CommandException(CommandException.ID.INVALID_ARGUMENT);
		int[] rows;
		if (has_dash) {
			String[] values = expr.split("-");
			if (values.length != 2)
				throw new CommandException(CommandException.ID.INVALID_ARGUMENT);
			type = DELETE_TYPE.BETWEEN;
			rows = new int[2];
			rows[0] = Integer.valueOf(values[0]);
			rows[1] = Integer.valueOf(values[1]);
		} else {
			String[] values = expr.split(",");
			type = DELETE_TYPE.INDIVIDUALLY;
			if (values.length < 1)
				throw new CommandException(CommandException.ID.INVALID_ARGUMENT);
			rows = new int[values.length];
			for (int i = 0; i < values.length; i++)
				rows[i] = Integer.valueOf(values[i]);
		}
		return rows;
	}
	
	public void execute(DBConnection db, Scanner in) {
		try {
			String query;
			if (type == DELETE_TYPE.BETWEEN) {
				query = Query.deleteBetween(rows);
			} else {
				query = Query.deleteIndividually(rows);
			}
			db.deleteNote(query);
		} catch (Exception ex) {
			ex.printStackTrace();
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
		try {
			String query = String.format(Query.insert(title, text));
			db.insertNote(query);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
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