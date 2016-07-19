package de.sebastiankings.renderengine.shaders;

import org.joml.Matrix4f;

import de.sebastiankings.renderengine.entities.Material;
import de.sebastiankings.renderengine.entities.PointLight;

import static org.lwjgl.opengl.GL20.glUniform1i;

public class EntityShaderProgram extends ShaderProgram {
	
	private static final String VERTEX_FILE   = "res/shaders/entity/vertexShader.glsl";
	private static final String FRAGMENT_FILE = "res/shaders/entity/fragmentShader.glsl";

	private int location_modelMatrix;
    private int location_viewMatrix;
    private int location_projectionMatrix;

    private int location_lightPos;
	private int location_lightColDiffuse;
	private int location_lightColAmbient;
	private int location_lightColSpecular;

	private int location_matEmission;
	private int location_matAmbient;
	private int location_matSpecular;
	private int location_matShininess;

	private int location_textureSampler;

	public EntityShaderProgram(String vertexPath, String fragmentPath) {
		super(vertexPath, fragmentPath);
	}

	@Override
	protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "normal");
		super.bindAttribute(2, "textureCoords");
		super.bindAttribute(3, "emission");
		super.bindAttribute(4, "ambient");
		super.bindAttribute(5, "specular");
		super.bindAttribute(6, "shininess");
		
	}

	@Override
	protected void getAllUniformLocations() {
		location_modelMatrix = super.getUniformLocation("modelMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");

		location_lightPos = super.getUniformLocation("lightPosition");
		location_lightColAmbient = super.getUniformLocation("lightAmbient");
		location_lightColDiffuse = super.getUniformLocation("lightDiffuse");
		location_lightColSpecular = super.getUniformLocation("lightSpecular");

		location_matEmission = super.getUniformLocation("matEmission");
		location_matAmbient = super.getUniformLocation("matAmbient");
		location_matSpecular = super.getUniformLocation("matSpecular");
		location_matShininess = super.getUniformLocation("matShininess");

		location_textureSampler = super.getUniformLocation("textureSampler");
	}

	public void loadMaterial(Material mat){
		super.loadVector(location_matEmission, mat.getEmission());
		super.loadVector(location_matAmbient, mat.getAmbient());
		super.loadVector(location_matSpecular, mat.getSpecular());
		super.loadFloat(location_matShininess, mat.getShininess());
	}

	public void loadLight(PointLight light) {
		super.loadVector(location_lightPos, light.getLightPos());
		super.loadVector(location_lightColAmbient, light.getLightColAmbient());
		super.loadVector(location_lightColDiffuse, light.getLightColDiffuse());
		super.loadVector(location_lightColSpecular, light.getLightColSpecular());
	}

	public void loadTexture(){
		glUniform1i(location_textureSampler, 0);
	}

	public void loadModelMatrix(Matrix4f matrix){
		super.loadMatrix(location_modelMatrix, matrix);
	}

    public void loadViewMatrix (Matrix4f view){
        super.loadMatrix(location_viewMatrix, view);
    }
	
	public void loadProjectionMatrix(Matrix4f projection){
		super.loadMatrix(location_projectionMatrix, projection);
	}

	@Override
	protected void setFragDataLocations() {
	setFragDataLocation(0, "colorDiffuse");
	setFragDataLocation(1, "positionWorld");
	setFragDataLocation(2, "normalWorld");
	setFragDataLocation(3, "textureCoordst");
		
	}

}
