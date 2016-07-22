package de.sebastiankings.renderengine.entities;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL13;

import de.sebastiankings.renderengine.shaders.EntityShaderProgram;
import de.sebastiankings.renderengine.texture.Texture;

public class Entity extends BaseEntity {

	private Texture primaryTexture;
	
	private boolean useMultitexture = false;
	private Texture secondaryTexture;
	private Texture blendMap;

	public Entity(EntityType type, Model model) {
		super(type, model, new Matrix4f());
	}

	public Entity(EntityType type, Model model, Matrix4f modelMatrix) {
		super(type, model, modelMatrix);
	}

	private Entity(EntityType type, Model model, Texture texture) {
		super(type, model, new Matrix4f());
		this.primaryTexture = texture;
	}

	public Entity clone() {
		return new Entity(this.type, this.model, this.primaryTexture);
	}

	@Override
	public void render(EntityShaderProgram shader, Camera camera, PointLight light) {
		
	}

	
	public Texture getPrimaryTexture() {
		return primaryTexture;
	}

	public void setPrimaryTexture(Texture primaryTexture) {
		this.primaryTexture = primaryTexture;
	}

	public boolean useMultitexture() {
		return useMultitexture;
	}

	public void setUseMultitexture(boolean useMultitexture) {
		this.useMultitexture = useMultitexture;
	}
	public void moveEntityGlobal(Vector3f move) {
		this.entityState.getCurrentPosition().add(move);
		updateModelMatrix();
	}

	public void rotateX(float roation) {
		this.entityState.incrementRotationX(roation);
		updateModelMatrix();
	}

	public void rotateY(float roation) {
		this.entityState.incrementRotationY(roation);
		updateModelMatrix();
	}

	public void rotateZ(float roation) {
		this.entityState.incrementRotationZ(roation);
		updateModelMatrix();
	}

	public void scale(float scale) {
		this.entityState.scale(scale);
		updateModelMatrix();
	}

	private void updateModelMatrix() {
		Matrix4f mm = new Matrix4f();
		Vector3f position = new Vector3f(entityState.getCurrentPosition());
		mm.translate(position);
		mm.scale(this.entityState.getScaleX(), this.entityState.getScaleY(), this.entityState.getScaleZ());
		mm.rotateXYZ(this.entityState.getRotationX(), this.entityState.getRotationY(), this.entityState.getRotationZ());
		this.modelMatrix = mm;
	}

	public Texture getSecondaryTexture() {
		return secondaryTexture;
	}

	public void setSecondaryTexture(Texture secondaryTexture) {
		this.secondaryTexture = secondaryTexture;
	}

	public Texture getBlendMap() {
		return blendMap;
	}

	public void setBlendMap(Texture blendMap) {
		this.blendMap = blendMap;
	}

}
