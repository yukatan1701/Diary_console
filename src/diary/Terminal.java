package diary;

final class Terminal {
	private static int id_len = 4, date_len = 10, title_len = 20, text_len = 30;
	
	private static String getFormattedHeader() {
		String p = "%-", s = " | ";
		String id = p + id_len + "s";
		String date = p + date_len + "s";
		String title = p + title_len + "s";
		String text = p + text_len + "s";
		return id + s + date + s + title + s + text + "\n";
	}
	
	private static String getSubline() {
		String p = "%-", s = "+";
		String id = p + String.valueOf(id_len + 1) + "s";
		String date = p + String.valueOf(date_len + 2) + "s";
		String title = p + String.valueOf(title_len + 2) + "s";
		String text = p + String.valueOf(text_len + 1) + "s";
		String formatted = id + s + date + s + title + s + text + "\n";
		return String.format(formatted, "", "", "", "").replace(' ', '-'); 
	}
	
	static void printHeader() {
		System.out.printf(getFormattedHeader(), "ID", "DATE", "TITLE", "TEXT");
		System.out.print(getSubline());
	}
	
	static void printTableRow(int id, String date, String title, String text) {
		String short_text = text, short_title = title;
		if (title.length() > title_len)
			short_title = title.substring(0, title_len - 3).concat("...");
		if (text.length() > text_len)
			short_text = text.substring(0, text_len - 3).concat("...");
		System.out.printf(getFormattedHeader(), id, date, short_title,
				short_text);
	}
	
	static void printNote(int id, String date, String title, String text) {
		System.out.printf("Id: %d\nDate: %s\n", id, date);
        System.out.printf("Title: %s\nText: %s\n", title, text);
        //Terminal.printHeader();
        //System.out.printf("%-4d | %-10s | %-12s | %-16s\n", id, date, title, text);
	}
}
