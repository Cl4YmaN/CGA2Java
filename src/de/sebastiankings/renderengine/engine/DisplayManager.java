package de.sebastiankings.renderengine.engine;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFWErrorCallback.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.apache.log4j.Logger;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.libffi.Closure;

public class DisplayManager {

	private static final Logger LOGGER = Logger.getLogger(DisplayManager.class);
	private static int width = 1920;
	private static int height = 1080;
	private static float aspect = 16.0f / 9.0f;

	@SuppressWarnings("unused")
	private static GLFWErrorCallback errorCallback;
	@SuppressWarnings("unused")
	private static GLFWWindowSizeCallback windowCallback;
	@SuppressWarnings("unused")
	private static GLFWScrollCallback scrollCallback;
	@SuppressWarnings("unused")
	private static Closure debug;
	// The window handle
	private static long window;

	public static long getWindow() {
		return window;
	}

	public static int getWidth() {
		return width;
	}

	public static int getHeight() {
		return height;
	}

	public static long createDisplay() {
		LOGGER.info("Creating Display");
		glfwSetErrorCallback(errorCallback = createPrint((System.err)));
		initOpenGL();
		initWindow();
		initCallbackFunctions(); // set mouse and keyboard interaction
		debug = GLUtil.setupDebugMessageCallback(); // after
		LOGGER.debug("Your OpenGL version is " + glGetString(GL_VERSION));
		return window;

	}

	private static void initWindow() {
		// Das Fenster erzeugen.
		window = glfwCreateWindow(width, height, "Exercise 06 - Ghetto Soccer", glfwGetPrimaryMonitor(), NULL);
		if (window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		// Den GLFW Kontext aktuell machen.
		glfwMakeContextCurrent(window);

		// GL Kontext unter Berücksichtigung des Betriebssystems erzeugen.
		GL.createCapabilities();

		// Synchronize to refresh rate.
		glfwSwapInterval(0);

		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

		// Das Fenster sichtbar machen.
		glfwShowWindow(window);
	}

	private static void initCallbackFunctions() {

		glfwSetWindowSizeCallback(window, windowCallback = new GLFWWindowSizeCallback() {
			@Override
			public void invoke(long window, int width, int height) {
				updateWidthHeight(width, height);
			}
		});
	}

	private static void initOpenGL() {
		// GLFW Initialisieren. Die meisten GLFW-Funktionen funktionieren vorher
		// nicht.
		if (glfwInit() != GL_TRUE)
			throw new IllegalStateException("Unable to initialize GLFW");

		// Konfigurieren des Fensters
		glfwDefaultWindowHints(); // optional, die aktuellen Window-Hints sind
									// bereits Standardwerte
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // Das Fenster bleibt nach dem
												// Erzeugen versteckt.
		glfwWindowHint(GLFW_RESIZABLE, GL_FALSE); // Die Fenstergröße lässt sich
													// verändern.
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_COMPAT_PROFILE);

		glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GL_TRUE); // Windowhint für
															// den Debug Kontext
	}

	public static void updateDisplay() {
		glfwPollEvents();
		glViewport(0, 0, width, height);
		glfwSwapBuffers(window);
	}

	private static void updateWidthHeight(int w, int h) {
		width = w;
		height = (int) ((float) w / aspect);
		glfwSetWindowSize(window, width, height);
	}

	public static void closeDisplay() {
		glfwDestroyWindow(window);
	}
}
