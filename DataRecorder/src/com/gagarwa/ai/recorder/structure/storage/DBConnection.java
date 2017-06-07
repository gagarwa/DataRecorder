package com.gagarwa.ai.recorder.structure.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

/**
 * A Database Connection to the MySQL Database Storage Engine.
 *
 * @author Gitesh Agarwal
 */
public class DBConnection {

	/**
	 * The instance of a {@link DBConnection} to the MySQL Database for this
	 * project.
	 */
	private static DBConnection instance;

	/**
	 * The Database Connection to the MySQL Database.
	 */
	private Connection connection;

	/**
	 * Creates a new Database Connection.
	 */
	private DBConnection() {
		try {
			MysqlDataSource source = new MysqlDataSource();
			source.setUser("root");
			source.setPassword("");
			source.setDatabaseName("recorder");
			source.setCreateDatabaseIfNotExist(true);
			connection = source.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns an instance of {@link DBConnection}.
	 * 
	 * @return an instance of this class
	 */
	public static DBConnection getInstance() {
		if (instance == null)
			return new DBConnection();
		else
			return instance;
	}

	/**
	 * Returns the Database {@link Connection} to the MySQL Database.
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
