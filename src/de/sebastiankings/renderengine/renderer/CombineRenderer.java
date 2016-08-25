package de.sebastiankings.renderengine.renderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import de.sebastiankings.renderengine.bo.Szene;
import de.sebastiankings.renderengine.entities.Model;
import de.sebastiankings.renderengine.framebuffer.BlurFrameBufferObject;
import de.sebastiankings.renderengine.framebuffer.DepthOfFieldFrameBufferObject;
import de.sebastiankings.renderengine.shaders.CombineShaderProgram;
import de.sebastiankings.renderengine.utils.LoaderUtils;

public class CombineRenderer {

	private final float[] QUADPOSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };
	private final Model QUAD = LoaderUtils.loadToVao(QUADPOSITIONS);

	public void render(Szene szene, DepthOfFieldFrameBufferObject fbo, BlurFrameBufferObject softBlur, BlurFrameBufferObject strongBlur) {
		CombineShaderProgram combineShaderProgram = szene.getCombineShader();
		combineShaderProgram.start();
		combineShaderProgram.loadTextures();
		combineShaderProgram.showDof(szene.getShowDof());
		combineShaderProgram.showSplices(szene.getShowSplices());
		GL30.glBindVertexArray(QUAD.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		// Bild + Blendmasken
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbo.getBlurStrong());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbo.getBlurSoft());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbo.getFocalPlane());
		// BluredTextures
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, softBlur.getFrameDataTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, strongBlur.getFrameDataTexture());

		// DRAWCALL
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, QUAD.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);

		combineShaderProgram.stop();
	}
}
