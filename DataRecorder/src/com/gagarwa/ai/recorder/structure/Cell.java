package com.gagarwa.ai.recorder.structure;

import java.util.ArrayList;

/**
 * The cell to hold one single piece of data as a recording tool.
 *
 * @author Gitesh Agarwal
 */
public class Cell {

	/**
	 * The unique identification number of the cell for recording and database
	 * management.
	 */
	private int id;

	/**
	 * The ID tracker for unique ID management.
	 */
	private static int ID = 1;

	/** The name of the cell. */
	private String name;

	/** The list of double connected cells. */
	private ArrayList<Cell> dcon;

	/** The list of connected cells. */
	private ArrayList<Cell> con;

	/**
	 * Creates a new cell.
	 * 
	 * @param name
	 *            the cell name
	 */
	public Cell(String name) {
		id = ID;
		ID++;
		this.name = name;
		dcon = new ArrayList<Cell>();
		con = new ArrayList<Cell>();
	}

	/**
	 * Adds a new double connected cell to the cell.
	 * 
	 * @param dcell
	 *            the double connected cell
	 */
	public void addDCell(Cell dcell) {
		if (!dcon.contains(dcell))
			this.dcon.add(dcell);
	}

	/**
	 * Adds a new connected cell to the cell.
	 * 
	 * @param cell
	 *            the connected cell
	 */
	public void addCell(Cell cell) {
		if (!con.contains(cell))
			this.con.add(cell);
	}

	/**
	 * Adds a new triangle double connection to the cell.
	 * 
	 * @param dcell
	 *            the double connected cell
	 * @param cell
	 *            the cell
	 */
	public void addTriDCon(Cell dcell, Cell cell) {
		addDCell(dcell);
		addCell(cell);
	}

	/**
	 * Adds a new triangle connection to the cell.
	 * 
	 * @param cell1
	 *            the connected cell one
	 * @param cell2
	 *            the connected cell two
	 */
	public void addTriCon(Cell cell1, Cell cell2) {
		addCell(cell1);
		addCell(cell2);
	}

	/**
	 * Returns true if this cell is double connected to the given cell.
	 *
	 * @param cell
	 *            the cell
	 * @return true if cell is found
	 */
	public boolean isDConnected(Cell cell) {
		return dcon.contains(cell);
	}

	/**
	 * Returns true if this cell is connected to the given cell.
	 *
	 * @param cell
	 *            the cell
	 * @return true if cell is found
	 */
	public boolean isConnected(Cell cell) {
		return con.contains(cell);
	}

	/**
	 * Returns the list of double connected cells.
	 * 
	 * @return the dcon
	 */
	public ArrayList<Cell> getDCon() {
		return dcon;
	}

	/**
	 * Returns the list of connected cells.
	 * 
	 * @return the con
	 */
	public ArrayList<Cell> getCon() {
		return con;
	}

	/**
	 * Returns the ID of the cell.
	 * 
	 * @return the ID
	 */
	public int getID() {
		return id;
	}

	/**
	 * Returns the name of the cell.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the number of total links connected to this cell.
	 * 
	 * @return the number of links
	 */
	public int getLinks() {
		return dcon.size() + con.size();
	}

	/**
	 * Returns the output of the cell.
	 * 
	 * @return the output
	 */
	public String output() {
		String s = name + "\n";
		boolean connected = false;

		for (Cell e : dcon) {
			for (Cell n : con) {
				if (e.isConnected(n)) {
					connected = true;
					s += e.getName() + " -> " + n.getName() + "\n";
				}
			}

			if (!connected) {
				s += e.getName() + "\n";
			}

			connected = false;
		}

		for (int i = 0; i < con.size(); i++) {
			Cell e = con.get(i);
			for (int j = i + 1; j < con.size(); j++) {
				Cell n = con.get(j);
				if (e.isDConnected(n)) {
					s += e.getName() + " -> " + n.getName() + "\n";
				}
			}
		}

		return s;
	}

	/**
	 * Returns the string value of the cell.
	 * 
	 * @return the string value
	 */
	@Override
	public String toString() {
		String s = name + " ";
		s += "[Link Count = " + getLinks() + ", ";

		s += "Connections (";
		if (dcon.isEmpty())
			s += ", ";

		for (Cell e : dcon)
			s += e.getName() + ", ";
		s = s.substring(0, s.length() - 2) + "), ";

		s += "Links (";
		if (con.isEmpty())
			s += ", ";

		for (Cell e : con)
			s += e.getName() + ", ";
		s = s.substring(0, s.length() - 2) + ")]";
		return s;
	}

}
