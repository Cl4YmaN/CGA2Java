package de.sebastiankings.renderengine.framebuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class PassthroughFrameBufferObject extends AbsctractFrameBufferObject {

	private int diffuseTexture; // Difuse part
	private int normalTexture;
	private int positionTexture;
	private int textureCoordTexture;

	private int depthTexture;
	private int depthBuffer;
	
	public PassthroughFrameBufferObject(int width, int height) {
		super(width, height, 4);
		diffuseTexture = createTextureAttachment(width, height, GL30.GL_COLOR_ATTACHMENT0);
		positionTexture = createTextureAttachment(width, height, GL30.GL_COLOR_ATTACHMENT1);
		normalTexture = createTextureAttachment(width, height, GL30.GL_COLOR_ATTACHMENT2);
		textureCoordTexture = createTextureAttachment(width, height, GL30.GL_COLOR_ATTACHMENT3);
		depthBuffer = createDepthBufferAttachment(width, height);
		depthTexture = createDepthTextureAttachment(width, height);
	}

	public int getDiffuseTexture() {
		return diffuseTexture;
	}

	public int getDepthTexture() {
		return depthTexture;
	}

	public int getNormalTexture() {
		return normalTexture;
	}

	public int getPositionTexture() {
		return positionTexture;
	}

	public int getTextureCoordTexture() {
		return textureCoordTexture;
	}

	public int getDepthBuffer() {
		return depthBuffer;
	}

	@Override
	public void spezificCleanUp() {
		GL11.glDeleteTextures(diffuseTexture);
		GL11.glDeleteTextures(positionTexture);
		GL11.glDeleteTextures(normalTexture);
		GL11.glDeleteTextures(textureCoordTexture);
		GL11.glDeleteTextures(depthTexture);
		GL30.glDeleteRenderbuffers(depthBuffer);
		
	}
}
