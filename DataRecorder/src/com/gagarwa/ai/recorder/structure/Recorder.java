package com.gagarwa.ai.recorder.structure;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.TreeMap;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonString;
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
		deserialize();
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
		return c.output();
	}

	/**
	 * Serializes the recorder information to "data.csv".
	 */
	private void serialize() {
		try (FileOutputStream fos = new FileOutputStream("data.json"); JsonWriter writer = Json.createWriter(fos)) {
			JsonBuilderFactory factory = Json.createBuilderFactory(null);
			JsonArrayBuilder builder = factory.createArrayBuilder();
			for (String e : recorder.keySet())
				builder.add(e);
			JsonArray cells = builder.build();

			JsonObjectBuilder obuilder = factory.createObjectBuilder();
			for (Cell e : recorder.values()) {
				JsonArrayBuilder cbuilder = factory.createArrayBuilder();
				for (Cell c : e.getDCon())
					cbuilder.add(c.getData());
				obuilder.add(e.getData(), cbuilder.build());
			}
			JsonObject dlinks = obuilder.build();

			obuilder = factory.createObjectBuilder();
			for (Cell e : recorder.values()) {
				JsonArrayBuilder cbuilder = factory.createArrayBuilder();
				for (Cell c : e.getCon())
					cbuilder.add(c.getData());
				obuilder.add(e.getData(), cbuilder.build());
			}
			JsonObject links = obuilder.build();

			JsonObject data = factory.createObjectBuilder().add("cells", cells).add("dlinks", dlinks)
					.add("links", links).build();
			writer.writeObject(data);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Deserializes the recorder information in "data.csv" to the recorder.
	 */
	private void deserialize() {
		try (FileInputStream fis = new FileInputStream("data.json"); JsonReader reader = Json.createReader(fis)) {
			JsonObject data = reader.readObject();
			JsonArray cells = data.getJsonArray("cells");
			JsonObject dlinks = data.getJsonObject("dlinks");
			JsonObject links = data.getJsonObject("links");

			List<JsonString> cellsData = cells.getValuesAs(JsonString.class);
			for (JsonString e : cellsData)
				recorder.put(e.getString(), new Cell(e.getString()));

			for (String e : recorder.keySet()) {
				JsonArray dcon = dlinks.getJsonArray(e);
				List<JsonString> dconData = dcon.getValuesAs(JsonString.class);
				for (JsonString s : dconData)
					recorder.get(e).addDCell(recorder.get(s.getString()));
			}

			for (String e : recorder.keySet()) {
				JsonArray con = links.getJsonArray(e);
				List<JsonString> conData = con.getValuesAs(JsonString.class);
				for (JsonString s : conData)
					recorder.get(e.toString()).addCell(recorder.get(s.getString()));
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
