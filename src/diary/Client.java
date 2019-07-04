package diary;

import java.util.Scanner;
import diary.Command.COMMAND_TYPE;
import java.io.File;

public class Client {
	
	private static void checkDatabase(String dbname) {
		File dir = new File(dbname);
		if (!dir.exists()) {
			System.out.println("Creating diary... Please wait.");
			DBConnection dbconn = new DBConnection(dbname);
			dbconn.closeConnection();
		}
		System.out.println("Diary is opened.");
	}
	
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
		if (args.length > 0)
			dbname = args[0];
		checkDatabase(dbname);
		System.out.println("Enter \'help\' to see the list of commands.");
		processCommand(dbname);
		System.out.println("Bye!");
	}
}