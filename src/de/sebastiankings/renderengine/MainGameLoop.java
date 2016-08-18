package de.sebastiankings.renderengine;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.system.libffi.Closure;

import de.sebastiankings.renderengine.bo.Inputs;
import de.sebastiankings.renderengine.bo.Szene;
import de.sebastiankings.renderengine.engine.DisplayManager;
import de.sebastiankings.renderengine.entities.Camera;
import de.sebastiankings.renderengine.entities.Entity;
import de.sebastiankings.renderengine.entities.EntityFactory;
import de.sebastiankings.renderengine.entities.EntityType;
import de.sebastiankings.renderengine.entities.Material;
import de.sebastiankings.renderengine.entities.PointLight;
import de.sebastiankings.renderengine.framebuffer.FrameBufferObject;
import de.sebastiankings.renderengine.renderer.EntityRenderer;
import de.sebastiankings.renderengine.renderer.PassthroughRenderer;
import de.sebastiankings.renderengine.shaders.EntityShaderProgram;
import de.sebastiankings.renderengine.shaders.PassthroughShaderProgram;
import de.sebastiankings.renderengine.utils.TerrainUtils;

public class MainGameLoop {
	private static final Logger LOGGER = Logger.getLogger(MainGameLoop.class);
	@SuppressWarnings("unused")
	private static GLFWErrorCallback errorCallback;
	@SuppressWarnings("unused")
	private static GLFWKeyCallback keyCallback;
	@SuppressWarnings("unused")
	private static Closure debug;

	private static long windowId;
	private static Szene szene;

	public static void main(String[] args) {
		try {
			// Setup window
			init();

			// Create Szene
			List<Entity> entities = new ArrayList<Entity>();
			Entity gumba = EntityFactory.createEntity(EntityType.CHAR_GUMBA);
//			gumba.scale(0.01f);
			gumba.moveEntityGlobal(new Vector3f(4.0f));
			entities.add(gumba);
			entities.add(EntityFactory.createEntity(EntityType.CHAR_GUMBA_OLD));
			
			entities.add(TerrainUtils.generateTerrain(200, 100));
//			for (int i = 1; i < 5; i++) {
//				Entity gumba = EntityFactory.createEntity(EntityType.CHAR_MARIO);
//				gumba.moveEntityGlobal(new Vector3f(4.0f * i, -1.0f * i, 4.0f * i));
//				gumba.rotateY(30 * i);
//				entities.add(gumba);
//			}
			List<PointLight> lights = new ArrayList<PointLight>();
			lights.add(new PointLight(new Vector3f(50.0f,50.0f,50.0f), new Vector3f(1.0f), new Vector3f(1.0f), new Vector3f(1.0f)));
			Material m = new Material(new Vector3f(0.2f), new Vector3f(0.2f), new Vector3f(0.2f), 50.0f);
			Inputs inputs = new Inputs();
			inputs.registerInputs(windowId);
			szene = new Szene(entities, lights, new Camera(), inputs);
			EntityRenderer entityRenderer = new EntityRenderer(szene);
			PassthroughRenderer passthrough = new PassthroughRenderer();
			initShaderProgramms();
//			Terrain t = TerrainUtils.generateTerrain(500, 500);
			//INIT FBO
			FrameBufferObject fbo = new FrameBufferObject(DisplayManager.getWidth(),DisplayManager.getHeight());
			
			LOGGER.info("Start GameLoop");
			long lastStartTime = System.currentTimeMillis() - 10;
			
			
			
			while (glfwWindowShouldClose(windowId) == GL_FALSE) {
				// setDeltatime
				long deltaTime = System.currentTimeMillis() - lastStartTime;
				lastStartTime = System.currentTimeMillis();
				handleInputs(deltaTime);
				
				
				szene.getCamera().updateViewMatrix();
				fbo.bind();
				glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
				entityRenderer.render();
				fbo.unbind(1920, 1080);
				glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
				passthrough.render(szene,m,fbo);

				DisplayManager.updateDisplay();
			}
			LOGGER.info("Ending Gameloop! Cleaning Up");
			cleanUp();
			LOGGER.info("Finished cleaning! Goodbye!");

		} catch (Exception e) {
			LOGGER.error("There was an error!", e);
			e.printStackTrace();
		} finally {

		}
	}

	private static void init() {
		LOGGER.info("Initialize Game");
		// Alle OGL-Settings laden
		windowId = DisplayManager.createDisplay();
		loadOpenGlSettings();
	}

	private static void initShaderProgramms() {
		szene.setEntityShader(new EntityShaderProgram("res/shaders/entity/vertexShader.glsl", "res/shaders/entity/fragmentShader.glsl"));
		szene.setPassthrough(new PassthroughShaderProgram("res/shaders/passthrough/vertexShader.glsl", "res/shaders/passthrough/fragmentShader.glsl"));
	}

	/**
	 * Method to Handle all user Inputs,
	 * 
	 * @param deltaTime
	 *            Timedifference to lastFrame
	 */
	private static void handleInputs(long deltaTime) {
		Inputs inputs = szene.getInputs();
		Camera cam = szene.getCamera();

		// Mousemovement
		if (inputs.getMouse().hasNewValues()) {
			cam.incrementPhi((float) inputs.getDeltaX() * -0.0003f);
			cam.incrementTheta((float) inputs.getDeltaY() * -0.0003f);
			inputs.getMouse().markRead();
		}

		if (inputs.keyPressed(GLFW_KEY_ESCAPE)) {
			glfwSetWindowShouldClose(DisplayManager.getWindow(), GL_TRUE);
		}

		if (inputs.keyPressed(GLFW_KEY_A)) {
			cam.moveLeft();
		}
		if (inputs.keyPressed(GLFW_KEY_D)) {
			cam.moveRight();
		}
		// CLEAR ROTATION IF NOT LEFT OR RIGHT
		if (inputs.keyPressed(GLFW_KEY_W)) {
			cam.moveForward();
		}
		if (inputs.keyPressed(GLFW_KEY_S)) {
			cam.moveBackward();
		}
		if (inputs.keyPressed(GLFW_KEY_SPACE)) {
		}
	}

	private static void loadOpenGlSettings() {
		LOGGER.trace("Loading OGL-Settings");
		glClearColor(0.4f, 0.4f, 0.4f, 1);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glEnable(GL_DEPTH_TEST);
	}

	private static void cleanUp() {
		szene.getEntityShader().cleanUp();
		DisplayManager.closeDisplay();
	}

}
