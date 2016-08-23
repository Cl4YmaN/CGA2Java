package de.sebastiankings.renderengine.renderer;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import de.sebastiankings.renderengine.bo.Szene;
import de.sebastiankings.renderengine.entities.Material;
import de.sebastiankings.renderengine.entities.Model;
import de.sebastiankings.renderengine.framebuffer.PassthroughFrameBufferObject;
import de.sebastiankings.renderengine.shaders.PassthroughShaderProgram;
import de.sebastiankings.renderengine.utils.LoaderUtils;

public class PassthroughRenderer {

	private final float[] QUADPOSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };
	private final Model QUAD = LoaderUtils.loadToVao(QUADPOSITIONS);
		
	public PassthroughRenderer(){
		
	}
	
	public void render(Szene szene, Material m,PassthroughFrameBufferObject fbo){
		PassthroughShaderProgram passthroughShader = szene.getPassthrough();
		passthroughShader.start();
		passthroughShader.loadSliceLimits();
		passthroughShader.loadTextures();
		passthroughShader.loadMaterial(m);
		passthroughShader.loadLight(szene.getLights().get(0));
		Matrix4f p = szene.getCamera().getProjectionMatrix();
		Matrix4f v = szene.getCamera().getViewMatrix();
		Matrix4f transformation = p.mul(v);
		passthroughShader.loadTransformation(transformation);
		passthroughShader.loadViewMatrix(v);
		GL30.glBindVertexArray(QUAD.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbo.getDiffuseTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbo.getPositionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbo.getNormalTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbo.getTextureCoordTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbo.getDepthTexture());

		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, QUAD.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		passthroughShader.stop();
	}
}
