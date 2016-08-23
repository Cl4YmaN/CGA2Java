package de.sebastiankings.renderengine.framebuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class DepthOfFieldFrameBufferObject extends AbsctractFrameBufferObject {

	private int blurStrongfront;
	private int blurSoftFront;
	private int focalPlane;
	private int blurSoftBack;
	private int blurStrongBack;

	public DepthOfFieldFrameBufferObject(int width, int height) {
		super(width, height,5);
		blurStrongfront = createTextureAttachment(width, height, GL30.GL_COLOR_ATTACHMENT0);
		blurSoftFront = createTextureAttachment(width, height, GL30.GL_COLOR_ATTACHMENT1);
		focalPlane = createTextureAttachment(width, height, GL30.GL_COLOR_ATTACHMENT2);
		blurSoftBack = createTextureAttachment(width, height, GL30.GL_COLOR_ATTACHMENT3);
		blurStrongBack = createTextureAttachment(width, height, GL30.GL_COLOR_ATTACHMENT4);
	}

	public int getBlurStrongFront() {
		return blurStrongfront;
	}

	public void setBlurStrongFront(int blurStrongFront) {
		this.blurStrongfront = blurStrongFront;
	}

	public int getBlurSoftFront() {
		return blurSoftFront;
	}

	public void setBlurSoftFront(int blurSoftFront) {
		this.blurSoftFront = blurSoftFront;
	}

	public int getFocalPlane() {
		return focalPlane;
	}

	public void setFocalPlane(int focalPlane) {
		this.focalPlane = focalPlane;
	}

	public int getBlurSoftBack() {
		return blurSoftBack;
	}

	public void setBlurSoftBack(int blurSoftBack) {
		this.blurSoftBack = blurSoftBack;
	}

	public int getBlurStrongBack() {
		return blurStrongBack;
	}

	public void setBlurStrongBack(int blurHardBack) {
		this.blurStrongBack = blurHardBack;
	}

	@Override
	public void spezificCleanUp() {
		GL11.glDeleteTextures(blurStrongfront);
		GL11.glDeleteTextures(blurSoftFront);
		GL11.glDeleteTextures(focalPlane);
		GL11.glDeleteTextures(blurSoftBack);
		GL11.glDeleteTextures(blurStrongBack);
	}
}
