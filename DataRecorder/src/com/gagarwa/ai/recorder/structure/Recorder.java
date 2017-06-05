package com.gagarwa.ai.recorder.structure;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.TreeMap;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonWriter;

/**
 * The recorder and manager of the data.
 *
 * @author Gitesh Agarwal
 */
public class Recorder {

	/**
	 * The map implementation to manage cells.
	 */
	private TreeMap<String, Cell> recorder;

	/**
	 * Creates a new recorder to manage data cells.
	 */
	public Recorder() {
		StringComparator c = new StringComparator();
		recorder = new TreeMap<String, Cell>(c);
		// deserialize();
	}

	/**
	 * Inputs the given cell data into the recorder.
	 * 
	 * @param data
	 *            the cell data
	 */
	public void input(String data) {
		if (recorder.get(data) == null) {
			recorder.put(data, new Cell(data));
			serialize();
		}
	}

	/**
	 * Inputs the given data into the recorder.
	 * 
	 * @param link
	 *            the link connecting main to connector
	 * @param main
	 *            the main data piece
	 */
	public void input(String link, String main) {
		Cell clink = recorder.get(link);
		Cell cmain = recorder.get(main);

		if (clink == null) {
			clink = new Cell(link);
			recorder.put(link, clink);
		}
		if (cmain == null) {
			cmain = new Cell(main);
			recorder.put(main, cmain);
		}

		clink.addDCell(cmain);
		cmain.addDCell(clink);
		serialize();
	}

	/**
	 * Inputs the given data into the recorder.
	 * 
	 * @param link
	 *            the link connecting main to connector
	 * @param main
	 *            the main data piece
	 * @param ctr
	 *            the connector, extra piece of information
	 */
	public void input(String link, String main, String ctr) {
		Cell clink = recorder.get(link);
		Cell cmain = recorder.get(main);
		Cell cctr = recorder.get(ctr);

		if (clink == null) {
			clink = new Cell(link);
			recorder.put(link, clink);
		}
		if (cmain == null) {
			cmain = new Cell(main);
			recorder.put(main, cmain);
		}
		if (cctr == null) {
			cctr = new Cell(ctr);
			recorder.put(ctr, cctr);
		}

		clink.addTriDCon(cmain, cctr);
		cmain.addTriDCon(clink, cctr);
		cctr.addTriCon(clink, cmain);
		serialize();
	}

	/**
	 * Searches and returns the requested data if discovered.
	 * 
	 * @param cell
	 *            the cell
	 * @return the requested data
	 */
	public String output(String cell) {
		Cell c = recorder.get(cell);
		if (c == null)
			return "No Data Discovered!";
		return c.toString();
	}

	/**
	 * Serializes the recorder information to "data.csv".
	 */
	private void serialize() {
		try (FileOutputStream fos = new FileOutputStream(new File("data.json"));
				JsonWriter writer = Json.createWriter(fos)) {
			JsonObject cells = Json.createObjectBuilder().build();
			cells.putAll(recorder);

			JsonArray dlinks = Json.createArrayBuilder().build();
			for (Cell e : recorder.values())
				dlinks.addAll(e.getDCon());

			JsonArray links = Json.createArrayBuilder().build();
			for (Cell e : recorder.values())
				links.addAll(e.getCon());

			writer.writeObject(cells);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Deserializes the recorder information in "data.csv" to the recorder.
	 */
	private void deserialize() {
		try (Scanner scanner = new Scanner(new File("/WebContect/data.csv"))) {
			while (scanner.hasNextLine()) {

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
