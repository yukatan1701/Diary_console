package diary;

import java.util.Scanner;
import diary.Command.COMMAND_TYPE;
import diary.CommandException.ID;

public class Client {
	static String dbname = "diary";
	
	private static Command getCommandByName(String line) throws CommandException {
		String[] words = line.split(" ");
		String name = words[0];
		try {
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
		} catch (CommandException ex) {
			throw ex;
		}
		throw new CommandException(ID.UNKNOWN_COMMAND);
	}
	
	private static void processCommand() {
		Scanner in = new Scanner(System.in);
		while (true) {
			System.out.print("-> ");
			Command command;
			try {
				command = getCommandByName(in.nextLine());
			} catch (CommandException ex) {
				ex.printStackTrace();
				continue;
			}
			if (command.getType() == COMMAND_TYPE.EXIT)
				break;
			DBConnection dbconn = new DBConnection();
			try {
				command.execute(dbconn, in);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			dbconn.closeConnection();
			System.out.print("\n");
		}
		//DBConnection.shutdown();
		in.close();
	}
	
	public static void main(String[] args) {
		if (args.length > 1)
			dbname = args[1];
		System.out.println("Enter \'help\' to see the list of commands.");
		processCommand();
		System.out.println("Bye!");
	}
}
