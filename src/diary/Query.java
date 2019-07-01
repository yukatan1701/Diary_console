package diary;

import java.io.*;
import java.util.Scanner;

public final class Query {
	private static String dir = "query/";
	
	public static String createTable() throws IOException {
		FileReader fr = new FileReader(dir + "create_table");
		Scanner in = new Scanner(fr);
		String query = String.format(in.nextLine(), Client.dbname);
		in.close();
		fr.close();
		return query;
	}
	
	public static String selectAll() throws IOException {
		FileReader fr = new FileReader(dir + "select_all");
		Scanner in = new Scanner(fr);
		String query = String.format(in.nextLine(), Client.dbname);
		in.close();
		fr.close();
		return query;
	}
	
	public static String selectById(int id) throws IOException {
		FileReader fr = new FileReader(dir + "select_id");
		Scanner in = new Scanner(fr);
		String query = String.format(in.nextLine(), Client.dbname, id);
		in.close();
		fr.close();
		return query;
	}
	
	public static String deleteById() throws IOException {
		FileReader fr = new FileReader(dir + "delete_id");
		Scanner in = new Scanner(fr);
		String query = String.format(in.nextLine(), Client.dbname);
		in.close();
		fr.close();
		return query;
	}
	
	public static String insert(String title, String text) throws IOException {
		FileReader fr = new FileReader(dir + "insert_note");
		Scanner in = new Scanner(fr);
		title = title.replace("\'", "\'\'");
		String query = String.format(in.nextLine(), Client.dbname, title, text);
		in.close();
		fr.close();
		return query;
	}
}
