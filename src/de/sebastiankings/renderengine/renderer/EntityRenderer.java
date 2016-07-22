package de.sebastiankings.renderengine.renderer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import org.lwjgl.opengl.GL13;

import de.sebastiankings.renderengine.bo.Szene;
import de.sebastiankings.renderengine.entities.Entity;
import de.sebastiankings.renderengine.entities.Model;
import de.sebastiankings.renderengine.shaders.EntityShaderProgram;

public class EntityRenderer {

	private Szene szene;

	public EntityRenderer(Szene szene) {
		this.szene = szene;
	}

	public void render() {
		EntityShaderProgram shader = szene.getEntityShader();
		for (Entity entity : szene.getEntities()) {
			shader.start();
			shader.useMultiTexture(entity.useMultitexture());
			shader.loadTextures(entity.useMultitexture());
			shader.loadMatricies(szene.getCamera().getProjectionMatrix(), szene.getCamera().getViewMatrix(), entity.getModelMatrix());
			// bind VAO and activate VBOs //
			Model model = entity.getModel();
			// Activate VAO Data
			glBindVertexArray(model.getVaoID());
			// VERTICES
			glEnableVertexAttribArray(0);
			// NORMALS
			glEnableVertexAttribArray(1);
			// DIFFUSEMAP
			glEnableVertexAttribArray(2);
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, entity.getPrimaryTexture().getTextureID());
			if (entity.useMultitexture()) {
				GL13.glActiveTexture(GL13.GL_TEXTURE1);
				glBindTexture(GL_TEXTURE_2D, entity.getSecondaryTexture().getTextureID());
				GL13.glActiveTexture(GL13.GL_TEXTURE2);
				glBindTexture(GL_TEXTURE_2D, entity.getBlendMap().getTextureID());
			} else {
				GL13.glActiveTexture(GL13.GL_TEXTURE1);
				glBindTexture(GL_TEXTURE_2D, 0);
				GL13.glActiveTexture(GL13.GL_TEXTURE2);
				glBindTexture(GL_TEXTURE_2D,0);
			}
			glDrawElements(GL_TRIANGLES, model.getVertexCount(), GL_UNSIGNED_INT, 0);
			// CLEANUP VERTEX ARRAY ATTRIBUTES
			glDisableVertexAttribArray(0);
			glDisableVertexAttribArray(1);
			glDisableVertexAttribArray(2);
			// CLEANUP VERTEX ARRAY
			glBindVertexArray(0);
			shader.stop();
		}
	}
}
