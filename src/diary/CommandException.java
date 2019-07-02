package diary;

public class CommandException extends Exception {
	private static final long serialVersionUID = 5899592237697002338L;
	private ID id;
	static enum ID { UNKNOWN_COMMAND, MANY_ARGS, FEW_ARGS, INVALID_ARGUMENT };
	
	CommandException(ID ex_id) {
		id = ex_id;
	}
	
	public void printStackTrace() {
		if (id == ID.UNKNOWN_COMMAND) {
			System.out.println("Failed to recognize command.");
		} else if (id == ID.MANY_ARGS) {
			System.out.println("Too many arguments for this command.");
		} else if (id == ID.FEW_ARGS) {
			System.out.println("Too few arguments for this command.");
		} else {
			System.out.println("Unknown command error.");
		}
	}
}
