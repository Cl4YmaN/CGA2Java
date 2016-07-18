package de.sebastiankings.renderengine.renderer;

import de.sebastiankings.renderengine.bo.Szene;
import de.sebastiankings.renderengine.entities.Entity;

public class EntityRenderer {
	
	private Szene szene;
	
	public EntityRenderer(Szene szene){
		this.szene = szene;
	}

	public void render(){
		for (Entity entity : szene.getEntities()) {
			entity.render(szene.getEntityShader(), szene.getCamera(), szene.getLights().get(0));
		}
	}
}
