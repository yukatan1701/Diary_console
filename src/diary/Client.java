package diary;

import java.util.Scanner;
import diary.Command.COMMAND_TYPE;

public class Client {
	static String dbname = "diary";
	private static Command getCommandByName(String line) {
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
			return new Exit();
		if (name.equals("help"))
			return new Help(words);
		System.out.println("Failed to recognize command.");
		return null;
	}
	
	private static void processCommand() {
		Scanner in;
		while (true) {
			System.out.print("-> ");
			in = new Scanner(System.in);
			Command command = getCommandByName(in.nextLine());
			if (command == null)
				break;
			if (command.getType() == COMMAND_TYPE.EXIT)
				break;
			DBConnection dbconn = new DBConnection();
			command.execute(dbconn);
			dbconn.closeConnection();
			System.out.print("\n");
		}
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
