package com.pankaku.samples;


public class MyGameThread extends Thread {

	public float posU;
	public float posV;
	public boolean moveNow = false;
	
	public MyGameThread() {
		this.posU = 0.0f;
		this.posV = 0.0f;
	}

	@Override
	public void run() {
		while (true) {
			if (moveNow) {
				if (posU == 0.0f || posU == 0.2f) {
					posU += 0.2f;
				} else if (posU == 0.4f) {
					posU = 0.0f;
				}
			}
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
