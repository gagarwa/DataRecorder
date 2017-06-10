package com.gagarwa.ai.recorder.structure.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Provides convenience methods for building the database, and executing SQL
 * files.
 * 
 * @author Gitesh Agarwal
 */
public class SQLBuilder {

	/**
	 * The main implementation of {@link SQLBuilder} that rebuilds the database.
	 * 
	 * @param args
	 *            command line arguments (not used)
	 */
	public static void main(String[] args) {
		SQLBuilder.rebuild();
	}

	/**
	 * Rebuilds the sample database.
	 */
	public static void rebuild() {
		SQLBuilder.dropTables();
		SQLBuilder.createTables();
		SQLBuilder.insertValues();

	}

	/**
	 * Executes the <code>dropTables.sql</code> SQL file to drop/delete the
	 * database tables.
	 */
	public static void dropTables() {
		InputStream stream = SQLBuilder.class.getResourceAsStream("dropTables.sql");
		SQLBuilder.executeSQLFile(stream);
	}

	/**
	 * Executes the <code>createTables.sql</code> SQL file to create the
	 * database tables.
	 */
	public static void createTables() {
		InputStream stream;
		if (SQLConnection.SQL_Database == SQLDatabase.MySQL)
			stream = SQLBuilder.class.getResourceAsStream("createTablesMySQL.sql");
		else
			stream = SQLBuilder.class.getResourceAsStream("createTablesMSSQL.sql");

		SQLBuilder.executeSQLFile(stream);
	}

	/**
	 * Executes the <code>insertValues.sql</code> SQL file to populate the
	 * database with sample cells.
	 */
	public static void insertValues() {
		InputStream stream = SQLBuilder.class.getResourceAsStream("insertValues.sql");
		SQLBuilder.executeSQLFile(stream);
	}

	/**
	 * Executes the statements in the SQL file.
	 * 
	 * @param sqlFile
	 *            the SQL file
	 */
	public static void executeSQLFile(File sqlFile) {
		try (FileInputStream stream = new FileInputStream(sqlFile)) {
			SQLBuilder.executeSQLFile(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Executes the statements in the SQL file wrapped by the input stream.
	 * 
	 * @param stream
	 *            the input stream wrapping the SQL file
	 */
	public static void executeSQLFile(InputStream stream) {
		List<String> stmts = parseSQLFile(stream);
		SQLConnection conn = SQLConnection.getInstance();

		for (String stmt : stmts) {
			conn.executeUpdate(stmt);
		}
	}

	/**
	 * Parses the SQL file for SQL statements to return.
	 * 
	 * @param stream
	 *            the input stream wrapping the SQL file
	 * @return the list of SQL statements
	 */
	private static List<String> parseSQLFile(InputStream stream) {
		List<String> stmts = new ArrayList<String>();

		try (Scanner scanner = new Scanner(stream)) {
			String stmt = "";

			while (scanner.hasNextLine()) {
				stmt += scanner.nextLine();
				if (!stmt.isEmpty() && stmt.charAt(stmt.length() - 1) == ';') {
					stmts.add(stmt.substring(0, stmt.length() - 1));
					stmt = "";
				}
			}
		}

		return stmts;
	}

}
