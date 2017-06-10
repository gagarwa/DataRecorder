package com.gagarwa.ai.recorder.structure;

/**
 * The link connecting one source cell to another target cell.
 *
 * @author Gitesh Agarwal
 */
public class Link {

	/**
	 * The unique identification number of the link for recording and database
	 * management.
	 */
	private int id;

	/**
	 * The ID tracker for unique ID management.
	 */
	private static int ID = 1;

	/** The type of link connection. */
	private LinkType type;

	/** The source of the link. */
	private Cell source;

	/** The target of the link. */
	private Cell target;

	/**
	 * Creates a new link connection.
	 * 
	 * @param type
	 *            the type of link
	 * @param source
	 *            the source cell
	 * @param target
	 *            the target cell
	 */
	public Link(LinkType type, Cell source, Cell target) {
		id = ID;
		ID++;
		this.type = type;
		this.source = source;
		this.target = target;
	}

	/**
	 * Returns the ID of the link.
	 * 
	 * @return the ID
	 */
	public int getID() {
		return id;
	}

	/**
	 * Returns the type of link connection.
	 * 
	 * @return the link type
	 */
	public LinkType getLinkType() {
		return type;
	}

	/**
	 * Returns the source of the link.
	 * 
	 * @return the source
	 */
	public String getSource() {
		return source.getName();
	}

	/**
	 * Returns the target of the link.
	 * 
	 * @return the target
	 */
	public String getTarget() {
		return target.getName();
	}

	/**
	 * Returns the string value of the link.
	 * 
	 * @return the string value
	 */
	@Override
	public String toString() {
		String s = type + " ";
		s += "[Source = " + source.getName() + ", ";
		s += "Target = " + target.getName() + "]";
		return s;
	}

}
