package de.sebastiankings.renderengine.shaders;

import static org.lwjgl.opengl.GL20.*;

public class BlurShaderProgram extends ShaderProgram {

	private int location_blurDirection;
	private int location_frameData;
	private int location_targetResolution;

	public BlurShaderProgram(String vertexPath, String fragmentPath) {
		super(vertexPath, fragmentPath);
	}

	public void setBlurDirection(boolean blurDirection){
		super.loadFloat(location_blurDirection, blurDirection ? 1.0f : 0f);
	}
	
	public void setTargetResolution(float targetResolution){
		super.loadFloat(location_targetResolution, targetResolution);
	}

	@Override
	protected void getAllUniformLocations() {
		location_blurDirection = super.getUniformLocation("blurDirection");
		location_blurDirection = super.getUniformLocation("frameData");
		location_targetResolution = super.getUniformLocation("targetResolution");
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
