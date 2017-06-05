package com.gagarwa.ai.recorder.structure;

import java.util.ArrayList;

import javax.json.JsonValue;

/**
 * The cell to hold one single piece of data as a recording tool.
 *
 * @author Gitesh Agarwal
 */
public class Cell implements JsonValue {

	/** The list of connected cells. */
	private ArrayList<Cell> con;

	/** The list of double connected cells. */
	private ArrayList<Cell> dcon;

	/** The data contained within the cell. */
	private String data;

	/**
	 * Creates a new cell with the data to store.
	 * 
	 * @param data
	 *            the data to store
	 */
	public Cell(String data) {
		con = new ArrayList<Cell>();
		dcon = new ArrayList<Cell>();
		this.data = data;
	}

	/**
	 * Returns the type of JSON value (JSON Object).
	 *
	 * @return the type of JSON value
	 */
	@Override
	public ValueType getValueType() {
		return ValueType.OBJECT;
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
	 * Returns the list of connected cells.
	 * 
	 * @return the con
	 */
	public ArrayList<Cell> getCon() {
		return con;
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
	 * Returns the data.
	 * 
	 * @return the data
	 */
	public String getData() {
		return data;
	}

	/**
	 * Returns the string value of the cell.
	 * 
	 * @return the string value of the cell
	 */
	@Override
	public String toString() {
		String s = data + "\n";
		boolean connected = false;

		for (Cell e : dcon) {
			for (Cell n : con) {
				if (e.isConnected(n)) {
					connected = true;
					s += e.getData() + " -> " + n.getData() + "\n";
				}
			}

			if (!connected) {
				s += e.getData() + "\n";
			}

			connected = false;
		}

		for (int i = 0; i < con.size(); i++) {
			Cell e = con.get(i);
			for (int j = i + 1; j < con.size(); j++) {
				Cell n = con.get(j);
				if (e.isDConnected(n)) {
					s += e.getData() + " -> " + n.getData() + "\n";
				}
			}
		}

		return s;
	}

}
