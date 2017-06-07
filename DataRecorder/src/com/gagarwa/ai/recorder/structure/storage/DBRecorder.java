package com.gagarwa.ai.recorder.structure.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gagarwa.ai.recorder.structure.Cell;

/**
 * The SQL database operations for the cells of the data recorder.
 *
 * @author Gitesh Agarwal
 */
public class DBRecorder {

	/**
	 * The <code>DLINK</code> link type for double connections.
	 */
	public static final String DLINK = "DLINK";

	/**
	 * The <code>LINK</code> link type for single connections.
	 */
	public static final String LINK = "LINK";

	/**
	 * Adds a cell to the list of cells.
	 * 
	 * @param cell
	 *            the cell
	 */
	public static void addCell(Cell cell) {
		DBConnection dc = DBConnection.getInstance();
		Connection conn = dc.getConnection();

		try (PreparedStatement ps = conn.prepareStatement("INSERT INTO cells (name) VALUES (?)")) {
			ps.setString(1, cell.getData());
			dc.executeUpdate(ps);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds a DLink between the given cells to the list of links.
	 * 
	 * @param left
	 *            the left cell
	 * @param right
	 *            the right cell
	 */
	public static void addDLink(Cell left, Cell right) {
		DBConnection dc = DBConnection.getInstance();
		Connection conn = dc.getConnection();

		try (PreparedStatement ps = conn.prepareStatement(
				"INSERT INTO links (linkType, cellLeft, cellLeftID, cellRight, cellRightID) VALUES (?, ?, ?, ?, ?)")) {
			ps.setString(1, DLINK);
			ps.setString(2, left.getData());
			ps.setInt(3, left.getID());
			ps.setString(4, right.getData());
			ps.setInt(5, right.getID());
			dc.executeUpdate(ps);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds a Link between the given cells to the list of links.
	 * 
	 * @param left
	 *            the left cell
	 * @param right
	 *            the right cell
	 */
	public static void addLink(Cell left, Cell right) {
		DBConnection dc = DBConnection.getInstance();
		Connection conn = dc.getConnection();

		try (PreparedStatement ps = conn.prepareStatement(
				"INSERT INTO links (linkType, cellLeft, cellLeftID, cellRight, cellRightID) VALUES (?, ?, ?, ?, ?)")) {
			ps.setString(1, LINK);
			ps.setString(2, left.getData());
			ps.setInt(3, left.getID());
			ps.setString(4, right.getData());
			ps.setInt(5, right.getID());
			dc.executeUpdate(ps);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the list of cells with their links.
	 * 
	 * @return the list of cells
	 */
	public static List<Cell> getCells() {
		DBConnection dc = DBConnection.getInstance();
		Connection conn = dc.getConnection();

		List<Cell> cells = null;

		try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM cells"); ResultSet rs = dc.executeQuery(ps)) {
			cells = getCellList(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM links"); ResultSet rs = dc.executeQuery(ps)) {
			cells = addLinks(cells, rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return cells;
	}

	/**
	 * Parses the {@link ResultSet} from the <code>CELLS</code> table to a list
	 * of cells.
	 *
	 * @param rs
	 *            the {@link ResultSet}
	 * @return the list of cells
	 */
	private static List<Cell> getCellList(ResultSet rs) {
		List<Cell> cells = new ArrayList<Cell>();
		try {
			while (rs.next()) {
				String name = rs.getString("name");
				Cell cell = new Cell(name);
				cells.add(cell);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cells;
	}

	/**
	 * Parses the {@link ResultSet} from the <code>LINKS</code> table and the
	 * links between the cells.
	 * 
	 * @param cells
	 *            the list of cells
	 * @param rs
	 *            the {@link ResultSet}
	 * @return the list of cells
	 */
	private static List<Cell> addLinks(List<Cell> cells, ResultSet rs) {
		try {
			while (rs.next()) {
				String linkType = rs.getString("linkType");
				int cellLeftID = rs.getInt("cellLeftID");
				int cellRightID = rs.getInt("cellRightID");

				Cell cellLeft = cells.get(cellLeftID - 1);
				Cell cellRight = cells.get(cellRightID - 1);
				if (linkType.equals(DLINK)) {
					cellLeft.addDCell(cellRight);
					cellRight.addDCell(cellLeft);
				} else { // LINK
					cellLeft.addCell(cellRight);
					cellRight.addCell(cellLeft);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cells;
	}

}
