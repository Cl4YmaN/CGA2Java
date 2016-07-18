package de.sebastiankings.renderengine.renderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import de.sebastiankings.renderengine.bo.Szene;
import de.sebastiankings.renderengine.entities.Model;
import de.sebastiankings.renderengine.framebuffer.FrameBufferObject;
import de.sebastiankings.renderengine.utils.LoaderUtils;

public class PassthroughRenderer {

	private final float[] QUADPOSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };
	private final Model QUAD = LoaderUtils.loadToVao(QUADPOSITIONS);
		
	public PassthroughRenderer(){
		
	}
	
	public void render(Szene szene, FrameBufferObject fbo){
		szene.getPassthrough().start();
		szene.getPassthrough().loadTexture();
		GL30.glBindVertexArray(QUAD.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbo.getTextureId());
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, QUAD.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		szene.getPassthrough().stop();
	}
}
