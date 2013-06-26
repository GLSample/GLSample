package com.pankaku.samples;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class MyGLSurfaceView extends GLSurfaceView {

	private float mWidth;
	private float mHeight;
	private MyRenderer mMyRenderer;
	private MyGameThread mGameThread;
	
	public MyGLSurfaceView(Context context , MyGameThread gameThread) {
		super(context);
		this.mGameThread = gameThread;
		
		setFocusable(true); //touchEventを取得できるようにする
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//float x = (event.getX() / (float)mWidth) * 2.0f - 1.0f;
		//float y = (event.getY() / (float)mHeight) * -3.0f + 1.5f;
		
		/*if (event.getAction() == MotionEvent.ACTION_DOWN) {
			mGameThread.runStart();
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			//mGameThread.runStop();
			Log.d("STOP","UPNOW");
		}
		*/
		switch(event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mGameThread.runStart();
			Log.d("MotionEvent","DOWN");
			break;
		case MotionEvent.ACTION_UP:
			mGameThread.runStop();
			Log.d("MotionEvent", "UP");
			break;
		default:
			break;
		}
		
		/*ここをfalseにするとACTION_DOWNを取得後にfalseを返してしまい、
		 * その後追いかけるのをやめてしまうためtrueにする
		 * */
		return true;
	}
	
	@Override
	public void setRenderer(Renderer renderer) {
		super.setRenderer(renderer);
		this.mMyRenderer = (MyRenderer)renderer;
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder,int format, int w, int h) {
		super.surfaceChanged(holder, format, w, h);
		this.mWidth = w;
		this.mHeight = h;
	}
	
	
}
