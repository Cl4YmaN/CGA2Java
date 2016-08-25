package de.sebastiankings.renderengine.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.joml.Vector2f;
import org.joml.Vector3f;

import de.sebastiankings.renderengine.bo.Face;
import de.sebastiankings.renderengine.bo.FaceEntry;
import de.sebastiankings.renderengine.bo.OBJData;
import de.sebastiankings.renderengine.bo.OBJEntity;
import de.sebastiankings.renderengine.bo.OBJMaterial;

public class OBJLoaderOld {

	private static final Logger LOGGER = Logger.getLogger(OBJLoaderOld.class);

	public static OBJData loadModelFromObj(String rootPath) {
		String meshPath = rootPath + "mesh.obj";
		// Load file from drive
		LOGGER.debug("Loading Mesh from File: " + meshPath);
		FileReader fr = null;
		try {
			fr = new FileReader(new File(meshPath));
		} catch (FileNotFoundException e) {
			LOGGER.error("*OBJ-Datei " + meshPath + " konnte nicht gefunden werden.", e);
		}
		BufferedReader reader = new BufferedReader(fr);
		String line;
		Map<String, OBJMaterial> materialMap = new HashMap<String, OBJMaterial>();
		OBJEntity currentEntity = new OBJEntity();
		try {
			while (true) {
				line = reader.readLine();
				if (line == null) {
					break;
				}
				String[] currentLine = line.split(" ");
				// Kommentare werden ignoriert
				if (line.startsWith("# ") || line.equals("")) {
					continue;
				}
				// Pfad zur Materialbibliothek
				if (line.startsWith("mtllib ")) {
					LOGGER.trace("Materialbibliothek referenziert!");
					String libPath = currentLine[1];
					String fullPath = rootPath.concat(libPath.substring(2, libPath.length()));
					materialMap = parseMaterialMap(fullPath);
					// Vertex Eintrag
				} else if (line.startsWith("v ")) {
					Vector3f vertex = parseVector3f(currentLine);
					currentEntity.addVertex(vertex);
					// Vertex Eintrag
				} else if (line.startsWith("v ")) {
					Vector3f vertex = parseVector3f(currentLine);
					currentEntity.addVertex(vertex);
					// Texture Eintrag
				} else if (line.startsWith("vt ")) {
					Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
					currentEntity.addTextureCoord(texture);
					// Normalen Eintrag
				} else if (line.startsWith("vn ")) {
					Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					currentEntity.addNormal(normal);
					// ObjectName
				} else if (line.startsWith("o ") || line.startsWith("g ")) {
					currentEntity.setObjectDescription(currentLine[1]);
					// ObjectName
				} else if (line.startsWith("usemtl ")) {
					currentEntity.setUsedMaterial(currentLine[1]);
				} else if (line.startsWith("f ")) {
					// Verarbeitung aller Faces
					Face firstFace = new Face();
					firstFace.addFaceDataEntry(parseFaceEntry(currentLine[1]));
					firstFace.addFaceDataEntry(parseFaceEntry(currentLine[2]));
					firstFace.addFaceDataEntry(parseFaceEntry(currentLine[3]));
					currentEntity.addFace(firstFace);
					while (true) {
						line = reader.readLine();
						if (line == null || line.equals("")) {
							break;
						} else {
							currentLine = line.split(" ");
							Face currentFace = new Face();
							currentFace.addFaceDataEntry(parseFaceEntry(currentLine[1]));
							currentFace.addFaceDataEntry(parseFaceEntry(currentLine[2]));
							currentFace.addFaceDataEntry(parseFaceEntry(currentLine[3]));
							currentEntity.addFace(currentFace);
						}
					}
					// Aktuelles Objekt wurde vollständig aus Datei entnommen!
					break;
				} else {
					LOGGER.warn("Unknown linestart: " + currentLine[0]);
				}
			}
			LOGGER.debug("Parsed OBJ Data");
			OBJData result = new OBJData(currentEntity, materialMap);
			return result;
		} catch (Exception e) {
			LOGGER.error("Error while parsing OBJFile ", e);
			return null;
		}
	}

	private static Map<String, OBJMaterial> parseMaterialMap(String fileName) {
		LOGGER.debug("Loading Material Map");
		Map<String, OBJMaterial> result = new HashMap<String, OBJMaterial>();
		// Load file from drive
		FileReader fr = null;
		try {
			fr = new FileReader(new File(fileName));
		} catch (FileNotFoundException e) {
			LOGGER.error("Datei " + fileName + " konnte nicht gefunden werden.", e);
		}
		LOGGER.debug("Loaded! Start Parsing");
		BufferedReader reader = new BufferedReader(fr);
		String line;
		OBJMaterial currentMat = new OBJMaterial();
		try {
			while (true) {
				line = reader.readLine();
				if (line == null) {
					break;
				}
				String[] currentLine = line.split(" ");
				// Kommentare und leere Zeilen werden ignoriert
				if (line.startsWith("# ") || line.equals("")) {
					continue;
				}
				if (line.startsWith("illum ")) {
					currentMat.setLightingMode(Integer.parseInt(currentLine[1]));
					result.put(currentMat.getName(), currentMat);
					currentMat = new OBJMaterial();
				} else if (line.startsWith("newmtl ")) {
					currentMat.setName(currentLine[1]);
				} else if (line.startsWith("map_Kd ")) {
					LOGGER.debug("Found Texture Reference " + currentLine[1]);
					currentMat.setDiffuseMap(currentLine[1]);
				} else if (line.startsWith("Kd ")) {
					currentMat.setDiffuseColor(parseVector3f(currentLine));
				} else if (line.startsWith("Ka ")) {
					currentMat.setAmbientColor(parseVector3f(currentLine));
				} else if (line.startsWith("Ks ")) {
					currentMat.setSpecularColor(parseVector3f(currentLine));
				} else if (line.startsWith("Ns ")) {
					currentMat.setShininess(Float.parseFloat(currentLine[1]));
				} else {
					LOGGER.warn("Unknown line Start! " + line);
				}

			}
			if (result.isEmpty()) {
				result.put(currentMat.getName(), currentMat);
			}
			LOGGER.debug("File Parsed! Found Materials: " + result.size());
			return result;
		} catch (Exception e) {
			LOGGER.error("Fehler beim parsen der MTL Datei!", e);
			return null;
		}
	}

	private static FaceEntry parseFaceEntry(String entryText) {
		String[] data = entryText.split("/");
		int vertex = 0, texture = 0, normal = 0;
		if (!data[0].equals("")) {
			vertex = Integer.parseInt(data[0]);
		}
		if (!data[1].equals("")) {
			texture = Integer.parseInt(data[1]);
		}
		if (!data[2].equals("")) {
			normal = Integer.parseInt(data[2]);
		}
		FaceEntry result = new FaceEntry(vertex, texture, normal);
		return result;
	}

	/**
	 * Achtung- das Line Array muss die Größe 4 haben
	 * 
	 * @param line
	 * @return
	 */
	private static Vector3f parseVector3f(String[] line) {
		return new Vector3f(Float.parseFloat(line[1]), Float.parseFloat(line[2]), Float.parseFloat(line[3]));
	}
}
