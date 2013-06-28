package com.pankaku.samples;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLSurfaceView;
import android.util.Log;

public class MyRenderer implements GLSurfaceView.Renderer {

	// コンテキスト
	private Context mContext;

	private int mWidth;
	private int mHeight;

	// test
	private float x = 0.0f;
	private float y = -0.5f;
	//private float u = 0.0f;
	//private float v = 0.0f;
	private float t_w = 0.2f;
	private float t_h = 0.35f;
	private float textureSizeX = 0.5f;
	private float textureSizeY = 0.5f;
	
	// テクスチャを管理するためのID
	private int mSampleTexture;
	private int mNumberTexture;
	
	//fps表示
	private long mFpsCountStartTime = System.currentTimeMillis();
	private int mFramesInSecond = 0;
	private int mFps = 0;
	//
	
	private MyGameThread mGameThread;

	public MyRenderer(Context context, MyGameThread gameThread) {
		this.mContext = context;
		this.mGameThread = gameThread;
	}

	// 描画を行う部分を記述するメソッドを追加する
	public void renderMain(GL10 gl) {
		
		/*
		 * GraphicUtil.drawNumbers(gl, 0.0f, 0.0f, // 中心の座標 0.2f, 0.2f, //
		 * 1つの数字のサイズ mNumberTexture, // 数字の描画に使うテクスチャ 12345, // 描画したい数字 8, //
		 * 描画したい桁数 1.0f, 1.0f, 1.0f, 1.0f // 色はそのまま );
		 */

		GraphicUtil.drawTexture(gl, mGameThread.posX, mGameThread.posY, textureSizeX, textureSizeY, mSampleTexture, mGameThread.posU, mGameThread.posV,
				t_w, t_h, 1.0f, 1.0f, 1.0f,1.0f);
		
		//fps表示
		long nowTime = System.currentTimeMillis();
		//現在時刻との差分を計算
		long difference = nowTime - mFpsCountStartTime;
		//1秒経過していた倍位は、フレーム数のカウント終了
		if (difference >= 1000) {
			mFps = mFramesInSecond;
			mFramesInSecond = 0;
			mFpsCountStartTime = nowTime;
		}
		mFramesInSecond++;
		Log.d("fps", mFps+"");

		
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		gl.glViewport(0, 0, mWidth, mHeight);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(-1.0f, 1.0f, -1.5f, 1.5f, 0.5f, -0.5f);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();

		gl.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		renderMain(gl);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		this.mWidth = width;
		this.mHeight = height;

		Global.gl = gl;// GLコンテキストを保持する

		// テクスチャの生成を行う
		Resources res = mContext.getResources();
		this.mSampleTexture = GraphicUtil.loadTexture(gl, res,
				R.drawable.bear);
		if (mSampleTexture != 0) {
			Log.i(getClass().toString(), "texture load success! sample_tex");
		}
		
		this.mNumberTexture = GraphicUtil.loadTexture(gl, res,
				R.drawable.number_texture);
		if (mNumberTexture != 0) {
			Log.i(getClass().toString(), "texture load success! numbers_tex");
		}
		
		
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {

	}
	
	public void setPos(float x,float y) {
		this.x = x;
		this.y = y;
	}
}
