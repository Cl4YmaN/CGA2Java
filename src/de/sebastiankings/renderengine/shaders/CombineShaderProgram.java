package de.sebastiankings.renderengine.shaders;

import static org.lwjgl.opengl.GL20.*;

public class CombineShaderProgram extends ShaderProgram {

	private int location_blurDirection;
	private int location_frameData;

	public CombineShaderProgram(String vertexPath, String fragmentPath) {
		super(vertexPath, fragmentPath);
	}

	public void setBlurDirection(boolean blurDirection){
		super.loadFloat(location_blurDirection, blurDirection ? 1.0f : 0f);
	}

	@Override
	protected void getAllUniformLocations() {
		location_blurDirection = super.getUniformLocation("blurDirection");
		location_blurDirection = super.getUniformLocation("frameData");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");

	}

	public void loadTextures() {
		glUniform1i(location_frameData, 0);
	}
	
	@Override
	protected void setFragDataLocations() {

	}
}
