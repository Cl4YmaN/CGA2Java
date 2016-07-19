package de.sebastiankings.renderengine.framebuffer;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

public class FrameBufferObject {

	private int width;
	private int height;

	private int id;
	private int diffuseTexture; // Difuse part
	private int normalTexture;
	private int positionTexture;
	private int textureCoordTexture;

	private int depthTexture;
	private int depthBuffer;

	public FrameBufferObject(int width, int height) {
		id = createFrameBuffer();
		this.width = width;
		this.height = height;
		diffuseTexture = createTextureAttachment(width, height, GL30.GL_COLOR_ATTACHMENT0);
		positionTexture = createTextureAttachment(width, height, GL30.GL_COLOR_ATTACHMENT1);
		normalTexture = createTextureAttachment(width, height, GL30.GL_COLOR_ATTACHMENT2);
		textureCoordTexture = createTextureAttachment(width, height, GL30.GL_COLOR_ATTACHMENT3);
		depthBuffer = createDepthBufferAttachment(width, height);
		depthTexture = createDepthTextureAttachment(width, height);
	}

	private int createFrameBuffer() {
		int frameBuffer = GL30.glGenFramebuffers();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
		GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);
		IntBuffer drawBuffers = BufferUtils.createIntBuffer(4);
		drawBuffers.put(GL30.GL_COLOR_ATTACHMENT0);
		drawBuffers.put(GL30.GL_COLOR_ATTACHMENT1);
		drawBuffers.put(GL30.GL_COLOR_ATTACHMENT2);
		drawBuffers.put(GL30.GL_COLOR_ATTACHMENT3);
		drawBuffers.flip();
		GL20.glDrawBuffers(drawBuffers);
		return frameBuffer;
	}

	public void bind() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, id);
		GL11.glViewport(0, 0, width, height);
	}

	public void unbind(int resetWidth, int resetHeight) {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		GL11.glViewport(0, 0, resetWidth, resetHeight);
	}

	protected int createTextureAttachment(int width, int height, int colorAttachment) {
		int texture = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL30.GL_RGB32F, width, height, 0, GL11.GL_RGB, GL11.GL_FLOAT, (ByteBuffer) null);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, colorAttachment, texture, 0);
		return texture;
	}

	protected int createDepthTextureAttachment(int width, int height) {
		int texture = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT32, width, height, 0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, (ByteBuffer) null);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, texture, 0);
		return texture;
	}

	protected int createDepthBufferAttachment(int width, int height) {
		int depthBuffer = GL30.glGenRenderbuffers();
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBuffer);
		GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT, width, height);
		GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, depthBuffer);
		return depthBuffer;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getId() {
		return id;
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

	public void cleanUp() {// call when closing the game
		GL30.glDeleteFramebuffers(id);
		GL11.glDeleteTextures(diffuseTexture);
		GL11.glDeleteTextures(positionTexture);
		GL11.glDeleteTextures(normalTexture);
		GL11.glDeleteTextures(textureCoordTexture);
		GL11.glDeleteTextures(depthTexture);
		GL30.glDeleteRenderbuffers(depthBuffer);
	}

	@Override
	public String toString() {
		return "FrameBufferObject [width=" + width + ", height=" + height + ", id=" + id + ", textureId=" + diffuseTexture + ", depthTextureId=" + depthTexture + ", depthBufferId=" + depthBuffer + "]";
	}

}
