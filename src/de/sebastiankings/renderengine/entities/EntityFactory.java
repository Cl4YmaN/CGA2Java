package de.sebastiankings.renderengine.entities;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

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
		Model model = OBJLoader.loadObjModel(type.getFolderName());
		// Überprüfen ob textur verwendet werden soll;

		Texture texture = null;
		texture = LoaderUtils.loadTexture(type.getTexturePath());
		// TEST_DIMENSIONS
		result = new Entity(type, model);
		result.setUseMultitexture(false);
		result.setPrimaryTexture(texture);
		LOGGER.debug("New entity created");
		addEntityToCache(result);
		return result;
	}

	private static void addEntityToCache(Entity e) {
		entityCache.put(e.getType(), e);
		LOGGER.debug("Added entity with type " + e.getType() + " to cache");
	}

	private static void init() {
		LOGGER.info("Initialising Entityfactory");
		initialized = true;
		entityCache = new HashMap<EntityType, Entity>();
	}

}
