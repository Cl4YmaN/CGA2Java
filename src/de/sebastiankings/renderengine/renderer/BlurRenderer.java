package de.sebastiankings.renderengine.renderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import de.sebastiankings.renderengine.bo.Szene;
import de.sebastiankings.renderengine.entities.Model;
import de.sebastiankings.renderengine.framebuffer.BlurFrameBufferObject;
import de.sebastiankings.renderengine.shaders.BlurShaderProgram;
import de.sebastiankings.renderengine.utils.LoaderUtils;

public class BlurRenderer {

	private final float[] QUADPOSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };
	private final Model QUAD = LoaderUtils.loadToVao(QUADPOSITIONS);
	
	public void render(Szene szene, boolean blurDirection,BlurFrameBufferObject previousFBO){
		BlurShaderProgram blurShader = szene.getBlurShader();
		blurShader.start();
		blurShader.setBlurDirection(blurDirection);
		blurShader.setTargetResolution(blurDirection ? previousFBO.getWidth() : previousFBO.getHeight());
		
		GL30.glBindVertexArray(QUAD.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		//Activate FrameDataTexture
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, previousFBO.getFrameDataTexture());
		
		//DRAWCALL
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, QUAD.getVertexCount());
		
		//Cleanup
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		
	}
}
