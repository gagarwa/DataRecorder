package com.gagarwa.ai.recorder.structure;

import java.util.Comparator;

/**
 * The comparator for comparing strings.
 *
 * @author Gitesh Agarwal
 */
public class StringComparator implements Comparator<String> {

	/**
	 * Compares its two strings for order. Returns a negative integer, zero, or a positive
	 * integer as the first argument is less than, equal to, or greater than the second.
	 * 
	 * @param o1
	 *            the first string
	 * @param o2
	 *            the second string
	 */
	@Override
	public int compare(String o1, String o2) {
		return o1.compareToIgnoreCase(o2);
	}

}
