package de.sebastiankings.renderengine.bo;

import java.util.List;

import de.sebastiankings.renderengine.entities.Camera;
import de.sebastiankings.renderengine.entities.Entity;
import de.sebastiankings.renderengine.entities.PointLight;
import de.sebastiankings.renderengine.shaders.BlurShaderProgram;
import de.sebastiankings.renderengine.shaders.CombineShaderProgram;
import de.sebastiankings.renderengine.shaders.EntityShaderProgram;
import de.sebastiankings.renderengine.shaders.PassthroughShaderProgram;

public class Szene {

	private Inputs inputs;
	private Camera camera;

	private List<PointLight> lights;
	private List<Entity> entities;

	private EntityShaderProgram entityShader;
	private PassthroughShaderProgram passthrough;
	private BlurShaderProgram blurShader;
	private CombineShaderProgram combineShader;

	public void setBlurShader(BlurShaderProgram blurShader) {
		this.blurShader = blurShader;
	}

	public Szene(List<Entity> entities, List<PointLight> lights, Camera camera, Inputs inputs) {
		this.setEntities(entities);
		this.setLights(lights);
		this.setCamera(camera);
		this.setInputs(inputs);
	}

	public EntityShaderProgram getEntityShader() {
		return entityShader;
	}

	public void setEntityShader(EntityShaderProgram entityShader) {
		this.entityShader = entityShader;
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public Inputs getInputs() {
		return inputs;
	}

	public void setInputs(Inputs inputs) {
		this.inputs = inputs;
	}

	public List<PointLight> getLights() {
		return lights;
	}

	public void setLights(List<PointLight> lights) {
		this.lights = lights;
	}

	public List<Entity> getEntities() {
		return entities;
	}

	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}

	public PassthroughShaderProgram getPassthrough() {
		return passthrough;
	}

	public void setPassthrough(PassthroughShaderProgram passthrough) {
		this.passthrough = passthrough;
	}

	public BlurShaderProgram getBlurShader() {
		return this.blurShader;
	}

	public CombineShaderProgram getCombineShader() {
		return combineShader;
	}

	public void setCombineShader(CombineShaderProgram combineShader) {
		this.combineShader = combineShader;
	}

}
