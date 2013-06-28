package com.pankaku.samples;

import android.util.Log;

public class MyGameThread extends Thread {

	public float posU, posV;
	public float posX, posY;
	public boolean moveNow = false;
	public float tX, tY;
	public float speedX, speedY;

	public MyGameThread() {
		this.posU = 0.0f;
		this.posV = 0.0f;
		this.posX = 0.0f;
		this.posY = -0.5f;
		this.tX = 0.0f;
		this.tY = 0.0f;
	}

	@Override
	public void run() {
		while (true) {
			/*if (moveNow) {
				if (posU == 0.0f || posU == 0.2f) {
					posU += 0.2f;
				} else if (posU == 0.4f) {
					posU = 0.0f;
				}
			} else {
				posU = 0.0f;
			}*/
			
			move();
			try {
				Thread.sleep(17);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void move() {
		if (tX > posX) {
			posX += speedX;

		} else if (tX < posX) {
			posX -= speedX;
		}
		
		if (tY > posY) {
			posY += speedY;

		} else if (tY < posY) {
			posY -= speedY;
		}

	}

	public void runStart() {
		moveNow = true;
	}

	public void runStop() {
		moveNow = false;
		posU = 0.0f;
	}

}
