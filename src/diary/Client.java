package diary;

import java.util.Scanner;
import diary.Command.COMMAND_TYPE;

public class Client {
	
	private static void processCommand(String dbname) {
		Scanner in = new Scanner(System.in);
		while (true) {
			System.out.print("-> ");
			Command command;
			try {
				command = Command.getCommandByName(in.nextLine());
			} catch (CommandException ex) {
				ex.printStackTrace();
				continue;
			}
			if (command.getType() == COMMAND_TYPE.EXIT)
				break;
			DBConnection dbconn = new DBConnection(dbname);
			try {
				command.execute(dbconn, in);
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				dbconn.closeConnection();
			}
			System.out.print("\n");
		}
		in.close();
	}
	
	public static void main(String[] args) {
		String dbname = "diary";
		if (args.length > 1)
			dbname = args[1];
		System.out.println("Enter \'help\' to see the list of commands.");
		processCommand(dbname);
		System.out.println("Bye!");
	}
}
