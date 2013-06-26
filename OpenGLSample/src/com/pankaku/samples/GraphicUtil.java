package com.pankaku.samples;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Hashtable;

import javax.microedition.khronos.opengles.GL10;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class GraphicUtil {

	// 配列オブジェクトを保持する
	private static Hashtable<Integer, float[]> verticesPool = new Hashtable<Integer, float[]>();
	private static Hashtable<Integer, float[]> colorsPool = new Hashtable<Integer, float[]>();
	private static Hashtable<Integer, float[]> coordsPool = new Hashtable<Integer, float[]>();

	// バッファオブジェクトを保持する
	private static Hashtable<Integer, FloatBuffer> polygonVerticesPool = new Hashtable<Integer, FloatBuffer>();
	private static Hashtable<Integer, FloatBuffer> polygonColorsPool = new Hashtable<Integer, FloatBuffer>();
	private static Hashtable<Integer, FloatBuffer> polygonCoordsPool = new Hashtable<Integer, FloatBuffer>();

	public static final FloatBuffer makeVerticesBuffer(float[] arr) {
		FloatBuffer fb = null;
		if (polygonVerticesPool.containsKey(arr.length)) {
			fb = polygonVerticesPool.get(arr.length);
			fb.clear();
			fb.put(arr);
			fb.position(0);
			return fb;
		}
		fb = makeFloatBuffer(arr);
		polygonVerticesPool.put(arr.length, fb);
		return fb;
	}
	
	public static final FloatBuffer makeColorsBuffer(float[] arr) {
		FloatBuffer fb = null;
		if (polygonColorsPool.containsKey(arr.length)) {
			fb = polygonColorsPool.get(arr.length);
			fb.clear();
			fb.put(arr);
			fb.position(0);
			return fb;
		}
		fb = makeFloatBuffer(arr);
		polygonColorsPool.put(arr.length, fb);
		return fb;
	}
	
	public static final FloatBuffer makeCoordsBuffer(float[] arr) {
		FloatBuffer fb = null;
		if (polygonCoordsPool.containsKey(arr.length)) {
			fb = polygonCoordsPool.get(arr.length);
			fb.clear();
			fb.put(arr);
			fb.position(0);
			return fb;
		}
		fb = makeFloatBuffer(arr);
		polygonCoordsPool.put(arr.length, fb);
		return fb;
	}

	public static float[] getVertices(int n) {
		if (verticesPool.containsKey(n)) {
			return verticesPool.get(n);
		}

		float[] vertices = new float[n];
		verticesPool.put(n, vertices);
		return vertices;
	}

	public static float[] getColors(int n) {
		if (colorsPool.containsKey(n)) {
			return colorsPool.get(n);
		}

		float[] colors = new float[n];
		colorsPool.put(n, colors);
		return colors;
	}

	public static float[] getCoords(int n) {
		if (coordsPool.containsKey(n)) {
			return coordsPool.get(n);
		}

		float[] coords = new float[n];
		coordsPool.put(n, coords);
		return coords;
	}

	public static final void drawTexture(GL10 gl, float x, float y,
			float width, float height, int texture, float u, float v,
			float tex_w, float tex_h, float r, float g, float b, float a) {

		float[] vertices = getVertices(8);
		vertices[0] = -0.5f * width + x;
		vertices[1] = -0.5f * height + y;
		vertices[2] = 0.5f * width + x;
		vertices[3] = -0.5f * height + y;
		vertices[4] = -0.5f * width + x;
		vertices[5] = 0.5f * height + y;
		vertices[6] = 0.5f * width + x;
		vertices[7] = 0.5f * height + y;

		float[] colors = getColors(16);
		for (int i = 0; i < 16; i++) {
			colors[i++] = r;
			colors[i++] = g;
			colors[i++] = b;
			colors[i] = a;
		}

		float[] coords = getCoords(8);
		coords[0] = u;
		coords[1] = v + tex_h;
		coords[2] = u + tex_w;
		coords[3] = v + tex_h;
		coords[4] = u;
		coords[5] = v;
		coords[6] = u + tex_w;
		coords[7] = v;

		FloatBuffer polygonVertices = makeVerticesBuffer(vertices);
		FloatBuffer polygonColors = makeColorsBuffer(colors);
		FloatBuffer texCoords = makeCoordsBuffer(coords);

		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, polygonVertices);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, polygonColors);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoords);
		// テクスチャの透過
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
		//
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_TEXTURE_2D);
	}

	public static final void drawTexture(GL10 gl, float x, float y,
			float width, float height, int texture, float r, float g, float b,
			float a) {
		drawTexture(gl, x, y, width, height, texture, 0.0f, 0.0f, 1.0f, 1.0f,
				r, g, b, a);
	}

	public static final int loadTexture(GL10 gl, Resources resources, int resId) {
		int[] textures = new int[1];

		// Bitmapの作成
		Bitmap bmp = BitmapFactory.decodeResource(resources, resId, options);
		if (bmp == null) {
			return 0;
		}

		// OpenGL用のテクスチャを生成します
		gl.glGenTextures(1, textures, 0);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmp, 0);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);

		// OpenGLへの転送が完了したので、VMメモリ上に作成したBitmapを破棄する
		bmp.recycle();

		// TextureManagerに登録する
		TextureManager.addTexture(resId, textures[0]);

		return textures[0];
	}

	private static final BitmapFactory.Options options = new BitmapFactory.Options();
	static {
		options.inScaled = false;// リソースの自動リサイズをしない
		options.inPreferredConfig = Config.ARGB_8888;// 32bit画像として読み込む
	}

	public static final FloatBuffer makeFloatBuffer(float[] arr) {
		ByteBuffer bb = ByteBuffer.allocateDirect(arr.length * 4);
		bb.order(ByteOrder.nativeOrder());
		FloatBuffer fb = bb.asFloatBuffer();
		fb.put(arr);
		fb.position(0);
		return fb;
	}
}
