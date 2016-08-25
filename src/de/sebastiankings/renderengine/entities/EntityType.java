package de.sebastiankings.renderengine.entities;

public enum EntityType {

	BLOCK_BRICK("res/meshes/block_brick/block_brick.obj", "res/meshes/block_brick/block_brick.png"), BLOCK_QUESTION("res/meshes/block_question/block_question.obj", "res/meshes/block_question/block_question.png"), BLOCK_WALL("res/meshes/block_wall/block_wall.obj", "res/meshes/block_wall/block_wall.png"), CHAR_GUMBA("res/meshes/char_gumba/char_gumba.obj",
			"res/meshes/char_gumba/char_gumba.png"), CHAR_MARIO("res/meshes/char_mario/char_mario.obj", "res/meshes/char_mario/char_mario.png"), ITEM_MUSHROOM("res/meshes/item_mushroom/", ""), MOUNTAIN("res/meshes/mountain/", ""), CHAR_GUMBA_OLD("res/meshes/gumba/mesh.obj", "res/meshes/gumba/diffuseMap.png");
	/**
	 * Path to DataFolder
	 */
	private String folderName;
	private String texturePath;

	public String getFolderName() {
		return folderName;
	}

	public String getTexturePath() {
		return texturePath;
	}

	EntityType(String folderName, String texturePath) {
		this.folderName = folderName;
		this.texturePath = texturePath;
	}

}
