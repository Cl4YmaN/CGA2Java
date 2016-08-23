package de.sebastiankings.renderengine.framebuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class BlurFrameBufferObject extends AbsctractFrameBufferObject{

	private int frameDataTexture;
	
	public BlurFrameBufferObject(int width, int height) {
		super(width, height,1);
		setFrameDataTexture(createTextureAttachment(width, height, GL30.GL_COLOR_ATTACHMENT0));
	}

	@Override
	public void spezificCleanUp() {
		GL11.glDeleteTextures(frameDataTexture);
		
	}

	public int getFrameDataTexture() {
		return frameDataTexture;
	}

	public void setFrameDataTexture(int frameDataTexture) {
		this.frameDataTexture = frameDataTexture;
	}

}
