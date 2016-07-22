package de.sebastiankings.renderengine.entities;

import org.joml.Matrix4f;

import de.sebastiankings.renderengine.shaders.EntityShaderProgram;

public abstract class BaseEntity {

	/*
	 * Default Model data
	 */
	protected EntityType type;
	protected Model model;
	protected Matrix4f modelMatrix = new Matrix4f();
	protected EntityState entityState = new EntityState();
	protected boolean showEntity;

	public BaseEntity(EntityType type, Model model, Matrix4f modelMatrix) {
		this.type = type;
		this.model = model;
		this.modelMatrix = modelMatrix;
		this.showEntity = true;

	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public Matrix4f getModelMatrix() {
		return modelMatrix;
	}

	public void setModelMatrix(Matrix4f modelMatrix) {
		this.modelMatrix = modelMatrix;
	}

	public EntityState getEntityState() {
		return entityState;
	}

	public void setEntityState(EntityState entityState) {
		this.entityState = entityState;
	}

	public EntityType getType() {
		return type;
	}

	public boolean showEntity() {
		return showEntity;
	}

	public void setShowEntity(boolean show) {
		this.showEntity = show;
	}


	abstract public void render(EntityShaderProgram shader, Camera camera, PointLight light);

}
