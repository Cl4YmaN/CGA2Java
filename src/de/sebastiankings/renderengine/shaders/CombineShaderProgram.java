package de.sebastiankings.renderengine.shaders;

import static org.lwjgl.opengl.GL20.*;

public class CombineShaderProgram extends ShaderProgram {

	private int location_blurStrongt;
	private int location_blurSoft;
	private int location_focalPlane;
	private int location_blurTextureSoft;
	private int location_blurTextureStrong;

	private int location_showDof;
	private int location_showSplices;

	public CombineShaderProgram(String vertexPath, String fragmentPath) {
		super(vertexPath, fragmentPath);
	}

	@Override
	protected void getAllUniformLocations() {
		location_blurStrongt = super.getUniformLocation("blurStrongt");
		location_blurSoft = super.getUniformLocation("blurSoft");
		location_focalPlane = super.getUniformLocation("focalPlane");
		location_blurTextureSoft = super.getUniformLocation("blurTextureSoft");
		location_blurTextureStrong = super.getUniformLocation("blurTextureStrong");

		location_showDof = super.getUniformLocation("showDof");
		location_showSplices = super.getUniformLocation("showSplices");
	}

	public void showDof(boolean showDof) {
		super.loadFloat(location_showDof, showDof ? 1.0f : 0.0f);
	}

	public void showSplices(boolean showSplices) {
		super.loadFloat(location_showSplices, showSplices ? 1.0f : 0.0f);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");

	}

	public void loadTextures() {
		glUniform1i(location_blurStrongt, 0);
		glUniform1i(location_blurSoft, 1);
		glUniform1i(location_focalPlane, 2);
		glUniform1i(location_blurTextureSoft, 3);
		glUniform1i(location_blurTextureStrong, 4);
	}

	@Override
	protected void setFragDataLocations() {

	}
}
