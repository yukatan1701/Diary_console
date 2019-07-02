package diary;

import java.io.*;
import java.util.Scanner;

public final class Query {
	private static String dir = "query/";
	
	private static String getQuery(String filename, Object... args)
				throws IOException {
		FileReader fr = new FileReader(dir + filename);
		Scanner in = new Scanner(fr);
		String query = String.format(in.nextLine(), args);
		in.close();
		fr.close();
		return query;
	}
	
	public static String createTable() throws IOException {
		return getQuery("create_table", Client.dbname);
	}
	
	public static String selectAll() throws IOException {
		return getQuery("select_all", Client.dbname);
	}
	
	public static String selectById(int id) throws IOException {
		return getQuery("select_id", Client.dbname, id);
	}
	
	public static String deleteIndividually(int[] rows) throws IOException {
		String suffix = "(" + String.valueOf(rows[0]);
		for (int i = 1; i < rows.length; i++)
			suffix += ", " + String.valueOf(rows[i]);
		suffix += ")";
		return getQuery("delete_individually", Client.dbname, suffix);
	}
	
	public static String deleteBetween(int[] rows) throws IOException {
		return getQuery("delete_range", Client.dbname, rows[0], rows[1]);
	}
	
	public static String insert(String title, String text) throws IOException {
		title = title.replace("\'", "\'\'");
		text = text.replace("\'", "\'\'");
		return getQuery("insert_note", Client.dbname, title, text);
	}
}
