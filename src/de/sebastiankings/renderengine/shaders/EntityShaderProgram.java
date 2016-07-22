package de.sebastiankings.renderengine.shaders;

import org.joml.Matrix4f;

import de.sebastiankings.renderengine.entities.Material;
import de.sebastiankings.renderengine.entities.PointLight;

import static org.lwjgl.opengl.GL20.glUniform1i;

public class EntityShaderProgram extends ShaderProgram {

	private static final String VERTEX_FILE = "res/shaders/entity/vertexShader.glsl";
	private static final String FRAGMENT_FILE = "res/shaders/entity/fragmentShader.glsl";

	private int location_modelMatrix;
	private int location_pvmMatrix;

	private int location_primaryTexture;

	// PARAMETERS FOR MULTITEXTURING
	private int location_useMultiTexture;
	private int location_secondaryTexture;
	private int location_blendMap;

	public EntityShaderProgram(String vertexPath, String fragmentPath) {
		super(vertexPath, fragmentPath);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "normal");
		super.bindAttribute(2, "textureCoords");
	}

	@Override
	protected void getAllUniformLocations() {
		location_modelMatrix = super.getUniformLocation("modelMatrix");
		location_pvmMatrix = super.getUniformLocation("pvmMatrix");

		location_primaryTexture = super.getUniformLocation("primaryTexture");

		// MULTITEXTURING
		location_useMultiTexture = super.getUniformLocation("useMultiTexture");
		location_secondaryTexture = super.getUniformLocation("secondaryTexture");
		location_blendMap = super.getUniformLocation("blendMap");
	}

	public void loadTextures(boolean useMultiTexture) {
		glUniform1i(location_primaryTexture, 0);
		if(useMultiTexture){
			glUniform1i(location_secondaryTexture, 1);
			glUniform1i(location_blendMap, 2);			
		}
	}

	public void loadMatricies(Matrix4f projection, Matrix4f view, Matrix4f model) {
		super.loadMatrix(location_modelMatrix, model);
		Matrix4f pvm = new Matrix4f();
		projection.mul(view, pvm);
		pvm.mul(model);
		super.loadMatrix(location_pvmMatrix, pvm);
	}
	
	public void useMultiTexture(boolean useMultitexture){
		super.loadFloat(location_useMultiTexture, useMultitexture ? 1 :0);
	}

	protected void setFragDataLocations() {
		setFragDataLocation(0, "colorDiffuse");
		setFragDataLocation(1, "positionWorld");
		setFragDataLocation(2, "normalWorld");

	}

}
