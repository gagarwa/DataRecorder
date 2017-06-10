package com.gagarwa.ai.recorder.structure;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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

import com.gagarwa.ai.recorder.structure.storage.SQLRecorder;

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

		List<Cell> cells = SQLRecorder.getCells();
		for (Cell e : cells)
			recorder.put(e.getName(), e);
	}

	/**
	 * Inputs the given cell data into the recorder.
	 * 
	 * @param data
	 *            the cell data
	 */
	public void input(String data) {
		if (recorder.get(data) == null) {
			Cell cell = new Cell(data);
			recorder.put(data, cell);
			SQLRecorder.addCell(cell);
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
			SQLRecorder.addCell(clink);
		}
		if (cmain == null) {
			cmain = new Cell(main);
			recorder.put(main, cmain);
			SQLRecorder.addCell(cmain);
		}

		clink.addDCell(cmain);
		cmain.addDCell(clink);
		SQLRecorder.addDLink(clink, cmain);
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
			SQLRecorder.addCell(clink);
		}
		if (cmain == null) {
			cmain = new Cell(main);
			recorder.put(main, cmain);
			SQLRecorder.addCell(cmain);
		}
		if (cctr == null) {
			cctr = new Cell(ctr);
			recorder.put(ctr, cctr);
			SQLRecorder.addCell(cctr);
		}

		clink.addTriDCon(cmain, cctr);
		cmain.addTriDCon(clink, cctr);
		cctr.addTriCon(clink, cmain);
		SQLRecorder.addDLink(clink, cmain);
		SQLRecorder.addLink(clink, cctr);
		SQLRecorder.addLink(cmain, cctr);
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
	 * Serializes the recorder information to a JSON String.
	 *
	 * @return the recorder information
	 */
	public String serialize() {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); JsonWriter writer = Json.createWriter(baos)) {
			List<Cell> cellList = new ArrayList<Cell>(recorder.values());
			JsonBuilderFactory factory = Json.createBuilderFactory(null);

			cellList.sort(new LinkComparator());
			List<Integer> linkGroup = new ArrayList<Integer>();
			List<Integer> linkCount = new ArrayList<Integer>();
			int nlinks = cellList.get(0).getLinks();
			int count = 0;
			for (Cell e : cellList) {
				if (nlinks == e.getLinks())
					count++;
				else {
					linkGroup.add(nlinks);
					linkCount.add(count);
					nlinks = e.getLinks();
					count = 1;
				}
			}
			linkGroup.add(nlinks);
			linkCount.add(count);

			JsonArrayBuilder builder = factory.createArrayBuilder();
			int group = 0;
			int i = 0;
			for (Cell e : cellList) {
				JsonObjectBuilder cbuilder = factory.createObjectBuilder();
				if (group != linkGroup.indexOf(e.getLinks())) {
					group++;
					i = 1;
				} else
					i++;
				cbuilder.add("name", e.getName());
				cbuilder.add("group", group + 1);
				cbuilder.add("radians", (double) i * (2 * Math.PI) / linkCount.get(group));
				builder.add(cbuilder.build());
			}
			JsonArray cells = builder.build();

			cellList.sort(new IDComparator());
			List<Link> linkList = SQLRecorder.getLinks(cellList);
			JsonArrayBuilder builderDL = factory.createArrayBuilder();
			JsonArrayBuilder builderL = factory.createArrayBuilder();

			for (Link e : linkList) {
				JsonObjectBuilder cbuilder = factory.createObjectBuilder();
				cbuilder.add("source", e.getSource());
				cbuilder.add("target", e.getTarget());
				if (e.getLinkType() == LinkType.DLINK)
					builderDL.add(cbuilder.build());
				else // LINK
					builderL.add(cbuilder.build());
			}
			JsonArray dlinks = builderDL.build();
			JsonArray links = builderL.build();

			JsonObject data = factory.createObjectBuilder().add("cells", cells).add("dlinks", dlinks)
					.add("links", links).build();
			writer.writeObject(data);

			baos.flush();
			return baos.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Deserializes the recorder information from a JSON String to the recorder.
	 * 
	 * @param recorderData
	 *            the JSON String
	 */
	public void deserialize(String recorderData) {
		try (ByteArrayInputStream bais = new ByteArrayInputStream(recorderData.getBytes());
				JsonReader reader = Json.createReader(bais)) {
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
