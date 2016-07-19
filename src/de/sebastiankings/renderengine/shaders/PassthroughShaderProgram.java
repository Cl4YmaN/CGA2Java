package de.sebastiankings.renderengine.shaders;

import static org.lwjgl.opengl.GL20.glUniform1i;

import org.joml.Matrix4f;

import de.sebastiankings.renderengine.entities.Material;
import de.sebastiankings.renderengine.entities.PointLight;

public class PassthroughShaderProgram extends ShaderProgram {

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

	public PassthroughShaderProgram(String vertexPath, String fragmentPath) {
		super(vertexPath, fragmentPath);
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

	public void loadViewMatrix (Matrix4f view){
        super.loadMatrix(location_viewMatrix, view);
    }
	@Override
	protected void setFragDataLocations() {

	}
}
