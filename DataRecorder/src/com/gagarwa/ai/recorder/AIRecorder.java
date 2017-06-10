package com.gagarwa.ai.recorder;

import java.util.Scanner;

import com.gagarwa.ai.recorder.structure.Recorder;

/**
 * The main implementation of the AI recorder.
 *
 * @author Gitesh Agarwal
 */
public class AIRecorder {

	/** The input command. */
	private static String INPUT = "<";

	/** The output command. */
	private static String OUTPUT = ">";

	/** The input format. */
	private static String FORM_IN = "INPUT FORMAT:  < [LINK] | [MAIN] | [CONNECTOR]";

	/** The output format. */
	private static String FORM_OUT = "OUTPUT FORMAT:  < [SUBJECT]";

	/**
	 * The AI recorder system to manage data.
	 */
	public Recorder air;

	/**
	 * Creates a new AI recorder system.
	 */
	public AIRecorder() {
		air = new Recorder();
	}

	/**
	 * Inputs the given data into the AI recorder system.
	 * 
	 * @param input
	 *            the input string
	 */
	public void input(String input) {
		String[] c = input.split(" \\| ");
		switch (c.length) {
		case 1:
			air.input(c[0]);
			break;
		case 2:
			air.input(c[0], c[1]);
			break;
		case 3:
			air.input(c[0], c[1], c[2]);
			break;
		default:
			throw new IllegalArgumentException("Illegal Number of Arguments! " + FORM_IN);
		}
	}

	/**
	 * Inputs the given data into the AI recorder system.
	 * 
	 * @param link
	 *            the link connecting main to connector
	 * @param main
	 *            the main data piece
	 * @param connector
	 *            the connector, extra piece of information
	 */
	public void input(String link, String main, String connector) {
		if (!link.isEmpty()) {
			if (!main.isEmpty()) {
				if (!connector.isEmpty())
					air.input(link, main, connector);
				else
					air.input(link, main);
			} else
				air.input(link);
		} else
			throw new IllegalArgumentException("Illegal Number of Arguments! " + FORM_IN);
	}

	/**
	 * Searches and returns the requested data if discovered.
	 * 
	 * @param input
	 *            the input string
	 * @return the requested data
	 */
	public String output(String input) {
		String[] c = input.split(" \\| ");
		switch (c.length) {
		case 1:
			return air.output(c[0]);
		default:
			throw new IllegalArgumentException("Illegal Number of Arguments! " + FORM_OUT);
		}
	}

	/**
	 * Returns the AI recorder system.
	 * 
	 * @return the AI recorder system
	 */
	public Recorder getRecorder() {
		return air;
	}

	/**
	 * The main implementation of the AI recorder.
	 * 
	 * @param args
	 *            command line arguments (not used)
	 */
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		AIRecorder ai = new AIRecorder();

		System.out.println("Running...");
		System.out.println(FORM_IN);
		System.out.println(FORM_OUT);

		while (scan.hasNext()) {
			String c = scan.next();
			try {
				if (c.equals(INPUT)) {
					ai.input(scan.nextLine().trim());
				} else if (c.equals(OUTPUT)) {
					System.out.println(ai.output(scan.nextLine().trim()));
				} else {
					System.out.println("Invalid Command!  Try Again.");
					System.out.println(FORM_IN);
					System.out.println(FORM_OUT);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		scan.close();
	}

}
