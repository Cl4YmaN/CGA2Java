package de.sebastiankings.renderengine.shaders;

import org.joml.Matrix4f;

import de.sebastiankings.renderengine.entities.Camera;

public class SkyboxShaderProgramm extends ShaderProgram {
	
    private int location_viewMatrix;
    private int location_projectionMatrix;

	public SkyboxShaderProgramm(String vertexShaderPath, String fragmentShaderPath) {
		super(vertexShaderPath, fragmentShaderPath);
	}
     
    public void loadProjectionMatrix(Matrix4f matrix){
        super.loadMatrix(location_projectionMatrix, matrix);
    }
 
    public void loadViewMatrix(Camera camera){
        Matrix4f matrix = new Matrix4f(camera.getViewMatrix());
        matrix.m30 = 0.0f;
        matrix.m31 = 0.0f;
        matrix.m32 = 0.0f;
        super.loadMatrix(location_viewMatrix, matrix);
    }
     
    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
    }
 
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }

	@Override
	protected void setFragDataLocations() {
		// TODO Auto-generated method stub
		
	}

}
