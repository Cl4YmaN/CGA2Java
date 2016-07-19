package de.sebastiankings.renderengine.shaders;

import static org.lwjgl.opengl.GL20.glUniform1i;

import org.joml.Matrix4f;

import de.sebastiankings.renderengine.entities.PointLight;

public class TerrainShaderProgramm extends ShaderProgram {
	
	private int location_modelMatrix;
    private int location_viewMatrix;
    private int location_projectionMatrix;

    private int location_lightPos;
	private int location_lightColDiffuse;
	private int location_lightColAmbient;
	private int location_lightColSpecular;

	private int location_textureSampler;

	public TerrainShaderProgramm(String vertexShaderPath, String fragmentShaderPath) {
		super(vertexShaderPath, fragmentShaderPath);
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
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");

		location_lightPos = super.getUniformLocation("lightPos");
		location_lightColAmbient = super.getUniformLocation("lightColAmbient");
		location_lightColDiffuse = super.getUniformLocation("lightColDiffuse");
		location_lightColSpecular = super.getUniformLocation("lightColSpecular");
		
		location_textureSampler = super.getUniformLocation("textureSampler");
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
		// TODO Auto-generated method stub
		
	}

}
