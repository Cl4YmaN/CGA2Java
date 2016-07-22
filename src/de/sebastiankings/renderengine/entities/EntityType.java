package de.sebastiankings.renderengine.entities;

public enum EntityType {

	BLOCK_BRICK("res/meshes/block_brick/"),
	BLOCK_QUESTION("res/meshes/block_question/"),
	BLOCK_WALL("res/meshes/block_wall/"),
	CHAR_GUMBA("res/meshes/char_gumba/"),
	CHAR_MARIO("res/meshes/char_mario/"),
	ITEM_MUSHROOM("res/meshes/item_mushroom/"),
	MOUNTAIN("res/meshes/mountain/"),
	CHAR_GUMBA_OLD("res/meshes/gumba/");
	/**
	 * Path to DataFolder
	 */
	private String folderName;

	EntityType(String folderName) {
		this.folderName = folderName;
	}

	public String getFolderPath() {
		return folderName;
	}


}
