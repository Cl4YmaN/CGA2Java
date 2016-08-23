package de.sebastiankings.renderengine.framebuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class DepthOfFieldFrameBufferObject extends AbsctractFrameBufferObject {

	private int blurStrong;
	private int blurSoft;
	private int focalPlane;

	public DepthOfFieldFrameBufferObject(int width, int height) {
		super(width, height,5);
		blurStrong = createTextureAttachment(width, height, GL30.GL_COLOR_ATTACHMENT0);
		blurSoft = createTextureAttachment(width, height, GL30.GL_COLOR_ATTACHMENT1);
		focalPlane = createTextureAttachment(width, height, GL30.GL_COLOR_ATTACHMENT2);
	}

	
	public int getFocalPlane() {
		return focalPlane;
	}

	public void setFocalPlane(int focalPlane) {
		this.focalPlane = focalPlane;
	}

	

	public int getBlurStrong() {
		return blurStrong;
	}


	public void setBlurStrong(int blurStrong) {
		this.blurStrong = blurStrong;
	}


	public int getBlurSoft() {
		return blurSoft;
	}


	public void setBlurSoft(int blurSoft) {
		this.blurSoft = blurSoft;
	}


	@Override
	public void spezificCleanUp() {
		GL11.glDeleteTextures(blurStrong);
		GL11.glDeleteTextures(blurSoft);
		GL11.glDeleteTextures(focalPlane);
	}
}
