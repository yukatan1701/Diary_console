package diary;

import java.sql.ResultSet;
import java.util.Scanner;
import java.io.*;
import java.util.Vector;
import java.util.Collections;
import diary.CommandException.ID;

interface Command {
	enum COMMAND_TYPE { ADD, ALL, CREATE, DELETE, EXIT, HELP, EDIT, SHOW };
	void execute(DBConnection db, Scanner in) throws Exception;
	public COMMAND_TYPE getType();
	
	public static Command getCommandByName(String line)
			throws CommandException {
		String[] words = line.split(" ");
		String name = words[0];
		if (name.equals("all"))
			return new All(words);
		if (name.equals("show"))
			return new Show(words);
		if (name.equals("add"))
			return new Add(words);
		if (name.equals("delete"))
			return new Delete(words);
		if (name.equals("exit"))
			return new Exit(words);
		if (name.equals("help"))
			return new Help(words);
		if (name.equals("create"))
			return new Create(words);
		if (name.equals("edit"))
			return new Edit(words);
		throw new CommandException(ID.UNKNOWN_COMMAND);
	}
}

class All implements Command {
	All(String[] words) throws CommandException {
		if (words.length > 1)
			throw new CommandException(ID.MANY_ARGS);
	}
	
	public void execute(DBConnection db, Scanner in) {
		try {
			ResultSet rs = db.getQueryResult(Query.selectAll(db.dbname));
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
			db.execute(Query.createTable(db.dbname));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	public COMMAND_TYPE getType() {
		return COMMAND_TYPE.CREATE;
	}
}

class Show implements Command {
	private int id;
	
	Show(String[] words) throws CommandException {
		if (words.length < 2)
			throw new CommandException(ID.FEW_ARGS);
		else if (words.length > 2)
			throw new CommandException(ID.MANY_ARGS);
		try {
			id = Integer.parseInt(words[1]);
		} catch (NumberFormatException ex) {
			throw new CommandException(ID.INVALID_ARGUMENT);
		}
	}
	
	public void execute(DBConnection db, Scanner in) {
		try {
			ResultSet rs = db.getQueryResult(Query.selectById(db.dbname, id));
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
			throw new CommandException(ID.FEW_ARGS);
		if (words.length == 2)
			rows = getRowsByExpression(words[1]);
		if (words.length > 2)
			throw new CommandException(ID.MANY_ARGS);
	}
	
	private int[] getRowsByExpression(String expr) throws CommandException {
		boolean has_dash = expr.contains("-");
		boolean has_comma = expr.contains(",");
		if (has_dash && has_comma)
			throw new CommandException(ID.INVALID_ARGUMENT);
		int[] rows;
		if (has_dash) {
			String[] values = expr.split("-");
			if (values.length != 2)
				throw new CommandException(ID.INVALID_ARGUMENT);
			type = DELETE_TYPE.BETWEEN;
			rows = new int[2];
			try {
				rows[0] = Integer.valueOf(values[0]);
				rows[1] = Integer.valueOf(values[1]);
			} catch (NumberFormatException ex) {
				throw new CommandException(ID.INVALID_ARGUMENT);
			}
		} else {
			String[] values = expr.split(",");
			type = DELETE_TYPE.INDIVIDUALLY;
			if (values.length < 1)
				throw new CommandException(ID.INVALID_ARGUMENT);
			rows = new int[values.length];
			for (int i = 0; i < values.length; i++) {
				try {
					rows[i] = Integer.valueOf(values[i]);
				} catch (NumberFormatException ex) {
					throw new CommandException(ID.INVALID_ARGUMENT);
				}
			}
		}
		return rows;
	}
	
	public void execute(DBConnection db, Scanner in) {
		try {
			String query;
			if (type == DELETE_TYPE.BETWEEN) {
				query = Query.deleteBetween(db.dbname, rows);
			} else {
				query = Query.deleteIndividually(db.dbname, rows);
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
			throw new CommandException(ID.MANY_ARGS);
	}
	
	public void execute(DBConnection db, Scanner in) {
		System.out.println("Title:");
		String title = in.nextLine();
		System.out.println("Text:");
		String text = in.nextLine();
		try {
			String query = Query.insert(db.dbname, title, text);
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
			throw new CommandException(ID.MANY_ARGS);
	}
	
	public void execute(DBConnection db, Scanner in) throws Exception {
		FileReader file = new FileReader("resources/help.txt");
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

class Edit implements Command {
	private enum EDIT_TYPE { ALL, TITLE, TEXT };
	EDIT_TYPE type = EDIT_TYPE.ALL;
	int id;
	
	Edit(String[] words) throws CommandException {
		try {
			id = Integer.parseInt(words[1]);
		} catch (NumberFormatException ex) {
			throw new CommandException(ID.INVALID_ARGUMENT);
		}
		if (words.length == 3) {
			if (words[2].equals("title"))
				type = EDIT_TYPE.TITLE;
			else if (words[2].equals("text"))
				type = EDIT_TYPE.TEXT;
			else
				throw new CommandException(ID.INVALID_ARGUMENT);
		} else if (words.length > 3)
			throw new CommandException(ID.MANY_ARGS);
	}
	
	public void execute(DBConnection db, Scanner in) {
		String title = "", text = "";
		if (type == EDIT_TYPE.ALL || type == EDIT_TYPE.TITLE) {
			System.out.println("New title:");
			title = in.nextLine();
		}
		if (type == EDIT_TYPE.ALL || type == EDIT_TYPE.TEXT) {
			System.out.println("New text:");
			text = in.nextLine();
		}
		try {
			if (type == EDIT_TYPE.ALL) {
				db.editNote(Query.editAll(db.dbname, id, title, text));
			} else if (type == EDIT_TYPE.TITLE) {
				db.editNote(Query.editField(db.dbname, id, "title", title));
			} else {
				db.editNote(Query.editField(db.dbname, id, "text", text));
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public COMMAND_TYPE getType() {
		return COMMAND_TYPE.EDIT;
	}
}

class Exit implements Command {
	Exit(String[] words) throws CommandException {
		if (words.length > 1)
			throw new CommandException(ID.MANY_ARGS);
	}
	
	public void execute(DBConnection db, Scanner in) {
		
	}
	
	public COMMAND_TYPE getType() {
		return COMMAND_TYPE.EXIT;
	}
}