package de.sebastiankings.renderengine.shaders;

import static org.lwjgl.opengl.GL20.glUniform1i;

import org.joml.Matrix4f;

public class PassthroughShaderProgram extends ShaderProgram {

	private int location_transformationMatrix;
	
	private int location_quadTexture;

	public PassthroughShaderProgram(String vertexPath, String fragmentPath) {
		super(vertexPath, fragmentPath);
	}

	public void loadTransformation(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_quadTexture = super.getUniformLocation("quadTexture");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");

	}
	public void loadTexture(){
		glUniform1i(location_quadTexture, 0);
	}
}
