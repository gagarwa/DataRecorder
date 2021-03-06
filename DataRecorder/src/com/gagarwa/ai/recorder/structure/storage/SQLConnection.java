package com.gagarwa.ai.recorder.structure.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

/**
 * A Database Connection to the SQL database storage engine.
 *
 * @author Gitesh Agarwal
 */
public class SQLConnection {

	/**
	 * The SQL Database Storage Engine to connect to.
	 */
	public static final SQLDatabase SQL_Database = SQLDatabase.MicrosoftSQL;

	/**
	 * The instance of a {@link SQLConnection} to the SQL database for this
	 * project.
	 */
	private static SQLConnection instance;

	/**
	 * The Database Connection to the SQL database.
	 */
	private Connection connection;

	/**
	 * Creates a new Database Connection.
	 */
	private SQLConnection() {
		if (SQL_Database == SQLDatabase.MySQL)
			createMySQLConnection();
		else
			createMSSQLConnection();
	}

	/**
	 * Creates a new MySQL Database Connection.
	 */
	private void createMySQLConnection() {
		try {
			MysqlDataSource source = new MysqlDataSource();
			source.setUser("root");
			source.setPassword("");
			source.setDatabaseName("recorder");
			source.setCreateDatabaseIfNotExist(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a new Microsoft SQL Database Connection.
	 */
	private void createMSSQLConnection() {
		try {
			SQLServerDataSource source = new SQLServerDataSource();
			source.setUser("gagarwa");
			source.setPassword("Sa!sa133");
			source.setDatabaseName("recorder");
			source.setServerName("recorder.database.windows.net");
			connection = source.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns an instance of {@link SQLConnection}.
	 * 
	 * @return an instance of this class
	 */
	public static SQLConnection getInstance() {
		if (instance == null)
			return new SQLConnection();
		else
			return instance;
	}

	/**
	 * Returns the Database {@link Connection} to the SQL database.
	 * 
	 * @return the connection
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * Executes a SQL query statement, and returns the {@link ResultSet}.
	 * 
	 * @param ps
	 *            the prepared SQL DML statement
	 * @return the {@link ResultSet}
	 */
	public ResultSet executeQuery(PreparedStatement ps) {
		try {
			return ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Executes a SQL query statement, and returns the {@link ResultSet}.
	 * 
	 * @param stmt
	 *            the SQL DML statement
	 * @return the {@link ResultSet}
	 */
	public ResultSet executeQuery(String stmt) {
		try (PreparedStatement ps = connection.prepareStatement(stmt)) {
			return ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Executes a SQL Data Manipulation Language (DML) statement, such as
	 * <code>INSERT</code>, <code>UPDATE</code> or <code>DELETE</code>.
	 * 
	 * @param ps
	 *            the prepared SQL DML statement
	 */
	public void executeUpdate(PreparedStatement ps) {
		try {
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Executes a SQL Data Manipulation Language (DML) statement, such as
	 * <code>INSERT</code>, <code>UPDATE</code> or <code>DELETE</code>.
	 * 
	 * @param stmt
	 *            the SQL DML statement
	 */
	public void executeUpdate(String stmt) {
		try (PreparedStatement ps = connection.prepareStatement(stmt)) {
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Closes the Database Connection.
	 */
	public void closeConnection() {
		try {
			if (!connection.isClosed())
				connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
