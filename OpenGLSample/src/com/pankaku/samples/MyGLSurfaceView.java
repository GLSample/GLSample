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
	private final static int INTERVAL = 60;

	public MyGLSurfaceView(Context context, MyGameThread gameThread) {
		super(context);
		this.mGameThread = gameThread;

		setFocusable(true); // touchEventを取得できるようにする
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = (event.getX() / (float) mWidth) * 2.0f - 1.0f;
		float y = (event.getY() / (float) mHeight) * -3.0f + 1.5f;

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mGameThread.runStart();
			mGameThread.tX = x;
			mGameThread.tY = y;
			
			Log.d("XY", "tX:" + mGameThread.tX + "tY:" + mGameThread.tY);
			
			if (mGameThread.tX > mGameThread.posX) {
				mGameThread.speedX = (mGameThread.tX - mGameThread.posX) / INTERVAL;
			} else if (mGameThread.tX < mGameThread.posX) {
				mGameThread.speedX = (mGameThread.posX - mGameThread.tX) / INTERVAL;
			}
			 //Log.d("Speed","SpeedX:"+mGameThread.speedX);
			
			if (mGameThread.tY > mGameThread.posY) {
				mGameThread.speedY = (mGameThread.tY - mGameThread.posY) / INTERVAL;
			} else if (mGameThread.tX < mGameThread.posX) {
				mGameThread.speedY = (mGameThread.posY - mGameThread.tY) / INTERVAL;
			}
			break;
		case MotionEvent.ACTION_UP:
			mGameThread.runStop();
			break;
		default:
			break;
		}

		/*
		 * ここをfalseにするとACTION_DOWNを取得後にfalseを返してしまい、 その後追いかけるのをやめてしまうためtrueにする
		 */
		return true;
	}

	@Override
	public void setRenderer(Renderer renderer) {
		super.setRenderer(renderer);
		this.mMyRenderer = (MyRenderer) renderer;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		super.surfaceChanged(holder, format, w, h);
		this.mWidth = w;
		this.mHeight = h;
	}

}
