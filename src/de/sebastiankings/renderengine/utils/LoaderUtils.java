package de.sebastiankings.renderengine.utils;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL13;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import de.sebastiankings.renderengine.entities.Model;
import de.sebastiankings.renderengine.entities.types.Skybox;
import de.sebastiankings.renderengine.texture.PNGData;
import de.sebastiankings.renderengine.texture.Texture;

public class LoaderUtils {
	private static final Logger LOGGER = Logger.getLogger(LoaderUtils.class);
	private static List<Integer> vaos = new ArrayList<Integer>();
	private static List<Integer> vbos = new ArrayList<Integer>();
	private static List<Integer> textures = new ArrayList<Integer>();

	public static Model loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices, float[] emission, float[] ambient, float[] specular, float[] shininess) {
		// create VAO and assign data
		int vaoID = createVAO();
		createIndexBuffer(indices);
		createVertexBuffer(0, 3, positions);
		createVertexBuffer(1, 3, normals);
		createVertexBuffer(2, 2, textureCoords);
		createVertexBuffer(3, 3, emission);
		createVertexBuffer(4, 3, ambient);
		createVertexBuffer(5, 3, specular);
		createVertexBuffer(6, 1, shininess);
		unbindVAO();

		// save VAO in RawModel
		return new Model(vaoID, indices.length);
	}
	
	public static Model loadToVao(float[] positions){
		int vaoID = createVAO();
		createVertexBuffer(0, 2, positions);
		unbindVAO();
		return new Model(vaoID, positions.length / 2);
	}

	public static Model loadTerrainVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
		// create VAO and assign data
		int vaoID = createVAO();
		createIndexBuffer(indices);
		createVertexBuffer(0, 3, positions);
		createVertexBuffer(1, 3, normals);
		createVertexBuffer(2, 2, textureCoords);
		unbindVAO();
		// save VAO in Model
		return new Model(vaoID, indices.length);
	}
	
	public static Model loadSkyboxVAO(float[] positions) {
		// create VAO and assign data
		int vaoID = createVAO();
		createVertexBuffer(0, 3, positions);
		unbindVAO();
		// save VAO in Model
		return new Model(vaoID, positions.length / 3);
	}

	public static Texture loadTexture(String fileName) {
		LOGGER.debug("Loading Texture: " + fileName);
		int textureID = 0;
		Texture texture = null;
		InputStream in = null;
		try {
			in = new FileInputStream(fileName);
			PNGDecoder decoder = new PNGDecoder(in);
			ByteBuffer buf = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
			decoder.decode(buf, decoder.getWidth() * 4, Format.RGBA);
			buf.flip();
			// create texture, activate and upload texture //
			textureID = glGenTextures();
			texture = new Texture(textureID);
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, textureID);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
			glGenerateMipmap(GL_TEXTURE_2D);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

		} catch (IOException e) {
			LOGGER.error("Error Loading Texture!", e);
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		textures.add(textureID);

		return texture;
	}

	public static Texture loadCubeMapTexture(String folderName){
		int textureID = glGenTextures();
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_CUBE_MAP, textureID);
		Texture texture = null;
		PNGData front = loadPngData(folderName + "/front.png");
		PNGData back = loadPngData(folderName+ "/back.png");
		PNGData left = loadPngData(folderName+ "/left.png");
		PNGData right = loadPngData(folderName+ "/right.png");
		PNGData bottom = loadPngData(folderName+ "/bottom.png");
		PNGData top = loadPngData(folderName+ "/top.png");

		
		// create texture, activate and upload texture //
		textureID = glGenTextures();
		texture = new Texture(textureID);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_CUBE_MAP, textureID);
		//Bind Actual Texture Data
		glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, GL_RGBA, front.getWidth(), front.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, front.getPictureData());
		glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, GL_RGBA, back.getWidth(), back.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, back.getPictureData());
		glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, GL_RGBA, left.getWidth(), left.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, left.getPictureData());
		glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, GL_RGBA, right.getWidth(), right.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, right.getPictureData());
		glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, GL_RGBA, bottom.getWidth(), bottom.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, bottom.getPictureData());
		glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, GL_RGBA, top.getWidth(), top.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, top.getPictureData());
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		texture = new Texture(textureID);
		return texture;
	}

	private static PNGData loadPngData(String fileName) {
		LOGGER.trace("Loading PNG-Data from file " + fileName);
		InputStream in = null;
		try {
			in = new FileInputStream(fileName);
			PNGDecoder decoder = new PNGDecoder(in);
			ByteBuffer buf = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
			decoder.decode(buf, decoder.getWidth() * 4, Format.RGBA);
			buf.flip();
			return new PNGData(decoder.getWidth(), decoder.getHeight(), buf);
		} catch (IOException e) {
			LOGGER.error("Error Loading Texture!", e);
			return new PNGData(-1, -1, ByteBuffer.allocateDirect(0));
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				LOGGER.error("Error while closing FileInputStream");
			}
		}
	}

	public static void cleanUp() {
		LOGGER.debug("Clean LoadingUtils");
		for (int vao : vaos) {
			glDeleteVertexArrays(vao);
		}
		for (int vbo : vbos) {
			glDeleteBuffers(vbo);
		}
		for (int texture : textures) {
			glDeleteTextures(texture);
		}
	}

	private static int createVAO() {
		int vaoID = glGenVertexArrays();
		vaos.add(vaoID);
		glBindVertexArray(vaoID);
		return vaoID;
	}

	private static void createVertexBuffer(int attributeNumber, int coordinateSize, float[] data) {
		// generate and save new ID for the vertex buffer object
		int vboID = glGenBuffers();
		vbos.add(vboID);

		// activate buffer and upload data
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

		// tell OpenGL how to interpret the data
		glVertexAttribPointer(attributeNumber, coordinateSize, GL_FLOAT, false, 0, 0);

		// unbind buffer
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	private static void unbindVAO() {
		glBindVertexArray(0);
	}

	private static void createIndexBuffer(int[] indices) {
		LOGGER.trace("Creating Indexbuffer");
		// generate and save new ID for the index buffer object
		int vboID = glGenBuffers();
		vbos.add(vboID);

		// activate buffer and upload data
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
	}

	public static IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	public static FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

}
