package de.sebastiankings.renderengine.shaders;

import static org.lwjgl.opengl.GL20.*;

import org.apache.log4j.Logger;
import org.joml.Matrix4f;

import de.sebastiankings.renderengine.entities.Material;
import de.sebastiankings.renderengine.entities.PointLight;

public class PassthroughShaderProgram extends ShaderProgram {
	private static final Logger LOGGER = Logger.getLogger(PassthroughShaderProgram.class);
	
	private int location_transformationMatrix;
	private int location_viewMatrix;

	private int location_diffuseTexture;
	private int location_positionTexture;
	private int location_normalTexture;
	private int location_textureCoordinateTexture;
	private int location_depthTexture;

	private int location_mat_ambient;
	private int location_mat_specular;
	private int location_mat_shininess;

	private int location_light_position;
	private int location_light_ambient;
	private int location_light_specular;
	private int location_light_diffuse;

	private int location_limitBlurStrongFront;
	private int location_limitBlurSoftFront;
	private int location_limitFocalPlane;
	private int location_limitBlurSoftBack;
	private int location_limitBlurStrongBack;

	public PassthroughShaderProgram(String vertexPath, String fragmentPath) {
		super(vertexPath, fragmentPath);
	}

	public void loadSliceLimits() {
		float near = 0.5f;
		float far = 2000.0f;
		float min = 1 / (near + far);
		float max = 1 / (near + far) - (far - near);
		float range = 1 - min;
		float tenPercent = range / 10;
		
		loadFloat(location_limitBlurStrongFront,tenPercent * 0.02f);
		loadFloat(location_limitBlurSoftFront,tenPercent * 0.1f);
		loadFloat(location_limitFocalPlane,tenPercent * 0.6f);
		loadFloat(location_limitBlurSoftBack,tenPercent * 0.9f);
		loadFloat(location_limitBlurStrongBack,tenPercent * 10);
		
//		LOGGER.debug(tenPercent * 1);
//		LOGGER.debug(tenPercent * 2);
//		LOGGER.debug(tenPercent * 6);
//		LOGGER.debug(tenPercent * 9);
//		LOGGER.debug(tenPercent * 10);
		

	}

	public void loadTransformation(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");

		location_diffuseTexture = super.getUniformLocation("diffuseTexture");
		location_positionTexture = super.getUniformLocation("positionTexture");
		location_normalTexture = super.getUniformLocation("normalTexture");
		location_textureCoordinateTexture = super.getUniformLocation("textureCoordinateTexture");
		location_depthTexture = super.getUniformLocation("depthTexture");

		location_mat_ambient = super.getUniformLocation("matAmbient");
		location_mat_specular = super.getUniformLocation("matSpecular");
		location_mat_shininess = super.getUniformLocation("matShininess");

		location_light_position = super.getUniformLocation("lightPosition");
		location_light_ambient = super.getUniformLocation("lightAmbient");
		location_light_specular = super.getUniformLocation("lightSpecular");
		location_light_diffuse = super.getUniformLocation("lightDiffuse");

		location_limitBlurStrongFront = super.getUniformLocation("limitBlurStrongFront");
		location_limitBlurSoftFront = super.getUniformLocation("limitBlurSoftFront");
		location_limitFocalPlane = super.getUniformLocation("limitFocalPlane");
		location_limitBlurSoftBack = super.getUniformLocation("limitBlurSoftBack");
		location_limitBlurStrongBack = super.getUniformLocation("limitBlurStrongBack");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");

	}

	public void loadTextures() {
		glUniform1i(location_diffuseTexture, 0);
		glUniform1i(location_positionTexture, 1);
		glUniform1i(location_normalTexture, 2);
		glUniform1i(location_textureCoordinateTexture, 3);
		glUniform1i(location_depthTexture, 4);
	}

	public void loadMaterial(Material mat) {
		super.loadVector(location_mat_ambient, mat.getAmbient());
		super.loadVector(location_mat_specular, mat.getSpecular());
		super.loadFloat(location_mat_shininess, mat.getShininess());
	}

	public void loadLight(PointLight light) {
		super.loadVector(location_light_position, light.getLightPos());
		super.loadVector(location_light_ambient, light.getLightColAmbient());
		super.loadVector(location_light_specular, light.getLightColDiffuse());
		super.loadVector(location_light_diffuse, light.getLightColSpecular());
	}

	public void loadViewMatrix(Matrix4f view) {
		super.loadMatrix(location_viewMatrix, view);
	}

	@Override
	protected void setFragDataLocations() {
		setFragDataLocation(0, "blurStrong");
		setFragDataLocation(1, "blurSoft");
		setFragDataLocation(2, "focalPlane");
		

	}
}
