package com.pankaku.samples;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

	private MyGameThread mGameThread;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// フルスクリーン、タイトルバーの非表示
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		this.mGameThread = new MyGameThread();
		Global.mainActivity = this;
		
		MyRenderer renderer = new MyRenderer(this,mGameThread);// MyRendererの生成
		MyGLSurfaceView glSurfaceView = new MyGLSurfaceView(this,mGameThread);// GLSurfaceViewの生成
		glSurfaceView.setRenderer(renderer);// GLSurfaceViewにMyRendererを適用

		setContentView(glSurfaceView);// ビューをGLSurfaceViewに配置
		
		mGameThread.start();
	}
	
	

	@Override
	public void onPause() {
		super.onPause();

		// テクスチャを削除する
		TextureManager.deleteAll(Global.gl);
	}
}