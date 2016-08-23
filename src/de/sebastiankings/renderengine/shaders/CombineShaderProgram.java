package de.sebastiankings.renderengine.shaders;

import static org.lwjgl.opengl.GL20.*;

public class CombineShaderProgram extends ShaderProgram {

	private int location_blurStrongFront;
	private int location_blurSoftFront;
	private int location_focalPlane;
	private int location_blurSoftBack;
	private int location_blurStrongBack;

	public CombineShaderProgram(String vertexPath, String fragmentPath) {
		super(vertexPath, fragmentPath);
	}

	@Override
	protected void getAllUniformLocations() {
		location_blurStrongFront = super.getUniformLocation("blurStrongFront");
		location_blurSoftFront = super.getUniformLocation("blurSoftFront");
		location_focalPlane = super.getUniformLocation("focalPlane");
		location_blurSoftBack = super.getUniformLocation("blurSoftBack");
		location_blurStrongBack = super.getUniformLocation("blurStrongBack");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");

	}

	public void loadTextures() {
		glUniform1i(location_blurStrongFront, 0);
		glUniform1i(location_blurSoftFront, 1);
		glUniform1i(location_focalPlane, 2);
		glUniform1i(location_blurSoftBack, 3);
		glUniform1i(location_blurStrongBack, 4);
	}

	@Override
	protected void setFragDataLocations() {

	}
}
