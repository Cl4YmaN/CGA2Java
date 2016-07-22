package de.sebastiankings.renderengine.entities;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import de.sebastiankings.renderengine.bo.OBJData;
import de.sebastiankings.renderengine.bo.OBJEntity;
import de.sebastiankings.renderengine.bo.OBJMaterial;
import de.sebastiankings.renderengine.engine.OBJLoader;
import de.sebastiankings.renderengine.texture.Texture;
import de.sebastiankings.renderengine.utils.LoaderUtils;

public class EntityFactory {

	private static final Logger LOGGER = Logger.getLogger(EntityFactory.class);
	private static boolean initialized = false;

	private static Map<EntityType, Entity> entityCache;

	public static Entity createEntity(EntityType type) {
		if (!initialized) {
			init();
		}
		Entity result = null;
		if (entityCache.containsKey(type)) {
			LOGGER.debug("Entity with type " + type.name() + " already loaded! Using cached Version!");
			return entityCache.get(type).clone();
		}
		OBJData data = OBJLoader.loadModelFromObj(type.getFolderPath());
		OBJEntity objEntity = data.getEntityData();
		LOGGER.debug("Processing Object: " + objEntity.getObjectDescription());
		LOGGER.debug("Converting VAO Data");
		objEntity.convertModelData();
		LOGGER.debug("Done");
		LOGGER.debug("Checking Material Data");
		// Überprüfen ob ein material angeben wurde
		OBJMaterial material = null;
		boolean hasTexture = false;
		if (objEntity.getUsedMaterial() != null) {
			LOGGER.debug("Material used! Converting Data");
			material = data.getMaterialByName(objEntity.getUsedMaterial());
			LOGGER.debug("Converted");
			hasTexture = material.getDiffuseMap() != null;
		} else {
			LOGGER.debug("No Material used! Loading Default");
			material = new OBJMaterial();
		}
		// Mesh und MaterialDatan in VAO speichern
		material.convertMaterial(objEntity.verticesArray.length);
		Model model = LoaderUtils.loadToVAO(objEntity.verticesArray, objEntity.texturesArray, objEntity.normalsArray, objEntity.indicesArray, material.emissionColorArray, material.ambientColorArray, material.specularColorArray, material.shininessArray);
		// Überprüfen ob textur verwendet werden soll;

		Texture texture = null;
		if (hasTexture) {
			LOGGER.debug("Texture used");
			String textureName = type.getFolderPath() + material.getDiffuseMap();
			texture = LoaderUtils.loadTexture(textureName);
			LOGGER.debug("Texture loaded");
		}
		//TEST_DIMENSIONS
		result = new Entity(type, model);
		result.setPrimaryTexture(texture);
		LOGGER.debug("New entity created");
		addEntityToCache(result);
		return result;
	}

	private static void addEntityToCache(Entity e){
		entityCache.put(e.getType(), e);
		LOGGER.debug("Added entity with type " + e.getType() + " to cache");
	}
	
	private static void init() {
		LOGGER.info("Initialising Entityfactory");
		initialized = true;
		entityCache = new HashMap<EntityType, Entity>();
	}

}
