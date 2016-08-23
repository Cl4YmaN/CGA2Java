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
			entities.add(EntityFactory.createEntity(EntityType.CHAR_GUMBA_OLD));
			entities.add(TerrainUtils.generateTerrain(200, 100));
			 for (int i = 1; i < 5; i++) {
			 Entity gumba = EntityFactory.createEntity(EntityType.CHAR_GUMBA_OLD);
			 gumba.moveEntityGlobal(new Vector3f(4.0f * i, -1.0f * i, 4.0f *
			 i));
			 gumba.rotateY(30 * i);
			 entities.add(gumba);
			 }
			List<PointLight> lights = new ArrayList<PointLight>();
			lights.add(new PointLight(new Vector3f(50.0f, 50.0f, 50.0f), new Vector3f(1.0f), new Vector3f(1.0f), new Vector3f(1.0f)));
			Material m = new Material(new Vector3f(0.2f), new Vector3f(0.2f), new Vector3f(0.2f), 50.0f);
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

			BlurFrameBufferObject horizontalBlurStrongFbo = new BlurFrameBufferObject(DisplayManager.getWidth() / 6, DisplayManager.getHeight() / 6);
			BlurFrameBufferObject verticalBlurStrongFbo = new BlurFrameBufferObject(DisplayManager.getWidth() / 6, DisplayManager.getHeight() / 6);
			
			BlurFrameBufferObject horizontalBlurSoftFbo = new BlurFrameBufferObject(DisplayManager.getWidth() / 2, DisplayManager.getHeight() / 2);
			BlurFrameBufferObject verticalBlurSoftFbo = new BlurFrameBufferObject(DisplayManager.getWidth() / 2, DisplayManager.getHeight() / 2);

			DepthOfFieldFrameBufferObject depthOfFieldInput = new DepthOfFieldFrameBufferObject(DisplayManager.getWidth(), DisplayManager.getHeight());
			DepthOfFieldFrameBufferObject depthOfFieldOutput = new DepthOfFieldFrameBufferObject(DisplayManager.getWidth(), DisplayManager.getHeight());

			LOGGER.info("Start GameLoop");
			long lastStartTime = System.currentTimeMillis() - 10;

			while (glfwWindowShouldClose(windowId) == GL_FALSE) {
				// setDeltatime
				long deltaTime = System.currentTimeMillis() - lastStartTime;
				lastStartTime = System.currentTimeMillis();
				handleInputs(deltaTime);

				szene.getCamera().updateViewMatrix();

				// render(m, entityRenderer, passthrough, blurRenderer, passthroughFbo, horizontalBlurFbo, verticalBlurFbo);
				renderDof(m, entityRenderer, passthrough, blurRenderer, combineRederer, passthroughFbo, depthOfFieldInput, depthOfFieldOutput, horizontalBlurStrongFbo, verticalBlurStrongFbo,horizontalBlurSoftFbo, verticalBlurSoftFbo);
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

	private static void render(Material m, EntityRenderer entityRenderer, PassthroughRenderer passthrough, BlurRenderer blurRenderer, PassthroughFrameBufferObject passthroughFbo, BlurFrameBufferObject horizontalBlurFbo, BlurFrameBufferObject verticalBlurFbo) {
		// GEOMETRY PASS
		passthroughFbo.bind();
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		entityRenderer.render();

		// SHADING PASS
		horizontalBlurFbo.bind();
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		passthrough.render(szene, m, passthroughFbo);

		// HORIZONTAL BLUR
		verticalBlurFbo.bind();
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		blurRenderer.render(szene, true, horizontalBlurFbo, horizontalBlurFbo.getFrameDataTexture());
		//
		// //VERTICAL BLUR
		verticalBlurFbo.unbind(DisplayManager.getWidth(), DisplayManager.getHeight());
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		blurRenderer.render(szene, false, verticalBlurFbo, verticalBlurFbo.getFrameDataTexture());

		// //RENDER TO SCREEN
		// glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		DisplayManager.updateDisplay();
	}

	private static void renderDof(Material m, EntityRenderer entityRenderer, PassthroughRenderer passthrough, BlurRenderer blurRenderer, CombineRenderer combineRenderer, PassthroughFrameBufferObject passthroughFbo, DepthOfFieldFrameBufferObject dofIn, DepthOfFieldFrameBufferObject dofOut, BlurFrameBufferObject horizontalBlurStrongFbo, BlurFrameBufferObject verticalBlurStrongFbo,BlurFrameBufferObject horizontalBlurSoftFbo, BlurFrameBufferObject verticalBlurSoftFbo) {
		// GEOMETRY PASS
		passthroughFbo.bind();
		entityRenderer.render();

		// SHADING PASS + SLICING FOR DOF
		dofIn.bind();
		passthrough.render(szene, m, passthroughFbo);

//		// BLUR SLICE FRONT STRONG
//		horizontalBlurFbo.bind();
//		blurRenderer.render(szene, true, dofIn, dofIn.getBlurStrongFront());
//		dofOut.bind();
//		blurRenderer.render(szene, false, horizontalBlurFbo, horizontalBlurFbo.getFrameDataTexture(), 0);
//
//		// BLUR SLICE FRONT WEAK
//		horizontalBlurFbo.bind();
//		blurRenderer.render(szene, true, dofIn, dofIn.getBlurSoftFront());
//		dofOut.bind();
//		blurRenderer.render(szene, false, horizontalBlurFbo, horizontalBlurFbo.getFrameDataTexture(), 1);
//
//		// BLUR SLICE FRONT STRONG
//		horizontalBlurFbo.bind();
//		blurRenderer.render(szene, true, dofIn, dofIn.getBlurStrongBack());
//		dofOut.bind();
//		blurRenderer.render(szene, false, horizontalBlurFbo, horizontalBlurFbo.getFrameDataTexture(), 3);
//
//		// BLUR SLICE FRONT WEAK
//		horizontalBlurFbo.bind();
//		blurRenderer.render(szene, true, dofIn, dofIn.getBlurSoftBack());
//		dofOut.bind();
//		blurRenderer.render(szene, false, horizontalBlurFbo, horizontalBlurFbo.getFrameDataTexture(), 4);

		// //RENDER TO SCREEN
		dofIn.unbind(DisplayManager.getWidth(), DisplayManager.getHeight());
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		combineRenderer.render(szene, dofIn);

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
