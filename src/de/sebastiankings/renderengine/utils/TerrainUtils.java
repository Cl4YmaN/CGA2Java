package de.sebastiankings.renderengine.utils;

import org.apache.log4j.Logger;

import de.sebastiankings.renderengine.entities.Entity;
import de.sebastiankings.renderengine.entities.Model;
import de.sebastiankings.renderengine.texture.Texture;

public class TerrainUtils {

	private static final Logger LOGGER = Logger.getLogger(TerrainUtils.class);

	public static Entity generateTerrain(float width, float length) {
		LOGGER.debug("Generating Terrain");
		float terrainHeight = -15f;
		float halfWidth = width / 2;
		float halflength = length / 2;
		float[] vertices = {
				// frontline
				-halfWidth, terrainHeight, halflength, halfWidth, terrainHeight, halflength,
				// horizontLine
				-halfWidth, terrainHeight, -halflength, halfWidth, terrainHeight, -halflength };

		// float[] textures = { 0.0f, 0.0f, 0.0f, 1.0f, (float) (length * 0.8 /
		// halfWidth), 0.0f, (float) (length * 0.8/ halfWidth), 1.0f, };
		float[] textures = { 0.0f, 0.0f, 0.0f, 3.0f, 3.0f, 0.0f, 3.0f, 3.0f, };

		float[] normals = { 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f };

		int[] indices = { 0, 1, 3, 0, 3, 2 };

		// *******************MULTITEXTURING**********************//
		Model terrainModel = LoaderUtils.loadTerrainVAO(vertices, textures, normals, indices);
		Texture terrainTexture1 = LoaderUtils.loadTexture("res/terrain/gras.png");
		Texture terrainTexture2 = LoaderUtils.loadTexture("res/terrain/way.png");
		Texture blendMap = LoaderUtils.loadTexture("res/terrain/blend.png");
		Entity e = new Entity(null, terrainModel);
		e.setPrimaryTexture(terrainTexture1);
		e.setSecondaryTexture(terrainTexture2);
		e.setBlendMap(blendMap);
		e.setUseMultitexture(true);
		LOGGER.debug("Terrain generated");
		return e;
	}

}
