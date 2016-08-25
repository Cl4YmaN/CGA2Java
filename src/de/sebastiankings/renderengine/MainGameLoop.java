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
import de.sebastiankings.renderengine.framebuffer.BlurFrameBufferObject;
import de.sebastiankings.renderengine.framebuffer.DepthOfFieldFrameBufferObject;
import de.sebastiankings.renderengine.framebuffer.PassthroughFrameBufferObject;
import de.sebastiankings.renderengine.renderer.BlurRenderer;
import de.sebastiankings.renderengine.renderer.CombineRenderer;
import de.sebastiankings.renderengine.renderer.EntityRenderer;
import de.sebastiankings.renderengine.renderer.PassthroughRenderer;
import de.sebastiankings.renderengine.shaders.BlurShaderProgram;
import de.sebastiankings.renderengine.shaders.CombineShaderProgram;
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
			entities.add(TerrainUtils.generateTerrain(400, 400));
			for (int i = -5; i < 5; i++) {
				for (int j = -5; j < 5; j++) {
					Entity gumba = EntityFactory.createEntity(EntityType.BLOCK_QUESTION);
					gumba.moveEntityGlobal(new Vector3f(50.0f * j, 30.0f, 50.0f * i));
					// gumba.rotateY(180.0f);
					entities.add(gumba);
				}
			}

			for (int i = -5; i < 5; i++) {
				for (int j = -5; j < 5; j++) {
					Entity gumba = null;
					if (i % 2 == 0) {
						gumba = EntityFactory.createEntity(EntityType.CHAR_MARIO);
					} else {
						gumba = EntityFactory.createEntity(EntityType.CHAR_GUMBA);
					}
					gumba.moveEntityGlobal(new Vector3f(50.0f * j, 0.0f, 50.0f * i));
					// gumba.rotateY(180.0f);
					entities.add(gumba);
				}
			}

			List<PointLight> lights = new ArrayList<PointLight>();
			lights.add(new PointLight(new Vector3f(500.0f, 500.0f, 500.0f), new Vector3f(1.0f), new Vector3f(1.0f), new Vector3f(1.0f)));
			Material m = new Material(new Vector3f(0.0f), new Vector3f(0.2f), new Vector3f(1f), 1050.0f);
			Inputs inputs = new Inputs();
			inputs.registerInputs(windowId);
			szene = new Szene(entities, lights, new Camera(), inputs);
			EntityRenderer entityRenderer = new EntityRenderer(szene);
			PassthroughRenderer passthrough = new PassthroughRenderer();
			BlurRenderer blurRenderer = new BlurRenderer();
			CombineRenderer combineRederer = new CombineRenderer();

			initShaderProgramms();

			// INIT FBOS
			PassthroughFrameBufferObject passthroughFbo = new PassthroughFrameBufferObject(DisplayManager.getWidth(), DisplayManager.getHeight());

			BlurFrameBufferObject horizontalBlurStrongFbo = new BlurFrameBufferObject(DisplayManager.getWidth() / 1, DisplayManager.getHeight() / 1);
			BlurFrameBufferObject verticalBlurStrongFbo = new BlurFrameBufferObject(DisplayManager.getWidth() / 1, DisplayManager.getHeight() / 1);

			BlurFrameBufferObject horizontalBlurSoftFbo = new BlurFrameBufferObject(DisplayManager.getWidth() / 2, DisplayManager.getHeight() / 2);
			BlurFrameBufferObject verticalBlurSoftFbo = new BlurFrameBufferObject(DisplayManager.getWidth() / 2, DisplayManager.getHeight() / 2);

			DepthOfFieldFrameBufferObject depthOfFieldInput = new DepthOfFieldFrameBufferObject(DisplayManager.getWidth(), DisplayManager.getHeight());
			DepthOfFieldFrameBufferObject depthOfFieldOutput = new DepthOfFieldFrameBufferObject(DisplayManager.getWidth(), DisplayManager.getHeight());

			LOGGER.info("Start GameLoop");
			long lastStartTime = System.currentTimeMillis() - 10;

			while (glfwWindowShouldClose(windowId) == GL_FALSE) {
				// setDeltatime
				long deltaTime = System.currentTimeMillis() - lastStartTime;
				// LOGGER.debug("Last Frametime: " + deltaTime);
				lastStartTime = System.currentTimeMillis();
				handleInputs(deltaTime);

				szene.getCamera().updateViewMatrix();

				renderDof(deltaTime, m, entityRenderer, passthrough, blurRenderer, combineRederer, passthroughFbo, depthOfFieldInput, depthOfFieldOutput, horizontalBlurStrongFbo, verticalBlurStrongFbo, horizontalBlurSoftFbo, verticalBlurSoftFbo);
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

	private static void renderDof(float deltatime, Material m, EntityRenderer entityRenderer, PassthroughRenderer passthrough, BlurRenderer blurRenderer, CombineRenderer combineRenderer, PassthroughFrameBufferObject passthroughFbo, DepthOfFieldFrameBufferObject dofIn, DepthOfFieldFrameBufferObject dofOut, BlurFrameBufferObject horizontalBlurStrongFbo, BlurFrameBufferObject verticalBlurStrongFbo,
			BlurFrameBufferObject horizontalBlurSoftFbo, BlurFrameBufferObject verticalBlurSoftFbo) {
		// GEOMETRY PASS
		passthroughFbo.bind();

		// Move Entities;
		for (int i = 0; i < szene.getEntities().size(); i++) {
			if (i == 0) {
				continue;
			}
			Entity entity = szene.getEntities().get(i);
			entity.rotateY((float) Math.toRadians(0.1) * deltatime);
		}
		entityRenderer.render();

		// SHADING PASS + SLICING FOR DOF
		dofIn.bind();
		passthrough.render(szene, m, passthroughFbo);

		// Soft blur
		horizontalBlurSoftFbo.bind();
		blurRenderer.render(szene, true, dofIn, dofIn.getFocalPlane());
		verticalBlurSoftFbo.bind();
		blurRenderer.render(szene, false, dofIn, horizontalBlurSoftFbo.getFrameDataTexture());

		// Strong blur
		horizontalBlurStrongFbo.bind();
		blurRenderer.render(szene, true, dofIn, verticalBlurSoftFbo.getFrameDataTexture());
		verticalBlurStrongFbo.bind();
		blurRenderer.render(szene, false, dofIn, horizontalBlurStrongFbo.getFrameDataTexture());

		// // //RENDER TO SCREEN
		verticalBlurSoftFbo.unbind(DisplayManager.getWidth(), DisplayManager.getHeight());
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		combineRenderer.render(szene, dofIn, verticalBlurSoftFbo, verticalBlurStrongFbo);

		DisplayManager.updateDisplay();
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
		szene.setBlurShader(new BlurShaderProgram("res/shaders/blur/vertexShader.glsl", "res/shaders/blur/fragmentShader.glsl"));
		szene.setCombineShader(new CombineShaderProgram("res/shaders/combine/vertexShader.glsl", "res/shaders/combine/fragmentShader.glsl"));
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

		// ENABLE DOF
		if (inputs.keyPressed(GLFW_KEY_U)) {
			szene.setShowDof(true);
		}
		// DISABLE DOF
		if (inputs.keyPressed(GLFW_KEY_J)) {
			szene.setShowDof(false);
		}

		// ENABLE DOF
		if (inputs.keyPressed(GLFW_KEY_I)) {
			szene.setShowSplices(true);
		}
		// DISABLE DOF
		if (inputs.keyPressed(GLFW_KEY_K)) {
			szene.setShowSplices(false);
		}

		// ENABLE DOF
		if (inputs.keyPressed(GLFW_KEY_O)) {
			szene.incrementFocalPlaneShift();
		}
		// DISABLE DOF
		if (inputs.keyPressed(GLFW_KEY_L)) {
			szene.decrementFocalPlaneShift();
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
