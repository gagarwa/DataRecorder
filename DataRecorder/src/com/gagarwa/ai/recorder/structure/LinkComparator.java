package com.gagarwa.ai.recorder.structure;

import java.util.Comparator;

/**
 * The comparator for comparing the number of links connected to cells.
 *
 * @author Gitesh Agarwal
 */
public class LinkComparator implements Comparator<Cell> {

	/**
	 * Compares the number of links of its two cells for order. A cell is
	 * greater than another if it has more links.
	 * 
	 * @param o1
	 *            the first cell
	 * @param o2
	 *            the second cell
	 * @return a negative integer, zero, or a positive integer as the first
	 *         argument is less than, equal to, or greater than the second
	 */
	@Override
	public int compare(Cell o1, Cell o2) {
		return o2.getLinks() - o1.getLinks();
	}

}
