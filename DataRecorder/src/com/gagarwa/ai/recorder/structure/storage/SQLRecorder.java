package com.gagarwa.ai.recorder.structure.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gagarwa.ai.recorder.structure.Cell;
import com.gagarwa.ai.recorder.structure.Link;
import com.gagarwa.ai.recorder.structure.LinkType;

/**
 * The SQL database operations for the cells of the data recorder.
 *
 * @author Gitesh Agarwal
 */
public class SQLRecorder {

	/**
	 * Adds a cell to the list of cells.
	 * 
	 * @param cell
	 *            the cell
	 */
	public static void addCell(Cell cell) {
		SQLConnection dc = SQLConnection.getInstance();
		Connection conn = dc.getConnection();

		try (PreparedStatement ps = conn.prepareStatement("INSERT INTO cells (name) VALUES (?)")) {
			ps.setString(1, cell.getName());
			dc.executeUpdate(ps);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds a DLink between the given cells to the list of links.
	 * 
	 * @param source
	 *            the source of the link
	 * @param target
	 *            the target of the link
	 */
	public static void addDLink(Cell source, Cell target) {
		SQLConnection dc = SQLConnection.getInstance();
		Connection conn = dc.getConnection();

		try (PreparedStatement ps = conn.prepareStatement(
				"INSERT INTO links (linkType, source, sourceID, target, targetID) VALUES (?, ?, ?, ?, ?)")) {
			ps.setString(1, LinkType.DLINK.toString());
			ps.setString(2, source.getName());
			ps.setInt(3, source.getID());
			ps.setString(4, target.getName());
			ps.setInt(5, target.getID());
			dc.executeUpdate(ps);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds a Link between the given cells to the list of links.
	 * 
	 * @param source
	 *            the source of the link
	 * @param target
	 *            the target of the link
	 */
	public static void addLink(Cell source, Cell target) {
		SQLConnection dc = SQLConnection.getInstance();
		Connection conn = dc.getConnection();

		try (PreparedStatement ps = conn.prepareStatement(
				"INSERT INTO links (linkType, source, sourceID, target, targetID) VALUES (?, ?, ?, ?, ?)")) {
			ps.setString(1, LinkType.LINK.toString());
			ps.setString(2, source.getName());
			ps.setInt(3, source.getID());
			ps.setString(4, target.getName());
			ps.setInt(5, target.getID());
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
		SQLConnection dc = SQLConnection.getInstance();
		Connection conn = dc.getConnection();

		List<Cell> cells = null;

		try (PreparedStatement ps = conn.prepareStatement("SELECT name FROM cells");
				ResultSet rs = dc.executeQuery(ps)) {
			cells = getCellList(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try (PreparedStatement ps = conn.prepareStatement("SELECT linkType, sourceID, targetID FROM links");
				ResultSet rs = dc.executeQuery(ps)) {
			cells = addLinks(cells, rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return cells;
	}

	/**
	 * Returns the list of link connections.
	 * 
	 * @param cells
	 *            the list of cells
	 * @return the list of links
	 */
	public static List<Link> getLinks(List<Cell> cells) {
		SQLConnection dc = SQLConnection.getInstance();
		Connection conn = dc.getConnection();

		List<Link> links = null;

		try (PreparedStatement ps = conn.prepareStatement("SELECT linkType, sourceID, targetID FROM links");
				ResultSet rs = dc.executeQuery(ps)) {
			links = getLinkList(rs, cells);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return links;
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
	 * Parses the {@link ResultSet} from the <code>LINKS</code> table to a list
	 * of links.
	 *
	 * @param rs
	 *            the {@link ResultSet}
	 * @param cells
	 *            the list of cells
	 * @return the list of links
	 */
	private static List<Link> getLinkList(ResultSet rs, List<Cell> cells) {
		List<Link> links = new ArrayList<Link>();
		try {
			while (rs.next()) {
				String linkType = rs.getString("linkType");
				int sourceID = rs.getInt("sourceID");
				int targetID = rs.getInt("targetID");

				Cell source = cells.get(sourceID - 1);
				Cell target = cells.get(targetID - 1);
				LinkType type = LinkType.valueOf(linkType);
				Link link = new Link(type, source, target);
				links.add(link);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return links;
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
				int sourceID = rs.getInt("sourceID");
				int targetID = rs.getInt("targetID");

				Cell source = cells.get(sourceID - 1);
				Cell target = cells.get(targetID - 1);
				if (linkType.equals(LinkType.DLINK.toString())) {
					source.addDCell(target);
					target.addDCell(source);
				} else { // LINK
					source.addCell(target);
					target.addCell(source);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cells;
	}

}
