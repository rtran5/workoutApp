package com.example.workoutappdemo;

import android.os.Handler;

import android.os.Looper;
import android.os.Message;

// From http://stackoverflow.com/a/16886486/119592
class LooperThread extends Thread {

	private Handler mainHandler_;
	private Helper helper_;

	LooperThread(Handler mainHandler, MainActivity activity) {
		mainHandler_ = mainHandler;
		helper_ = activity.helper_;
	}

	public Handler mHandler;
	public static final int WHAT_NETWORK_REQUEST = 0;

	public void run() {
		Looper.prepare();

		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {

				// What was requested of us?
				switch (msg.what) {
				case WHAT_NETWORK_REQUEST:
					helper_.doPotentiallyLongRunningBackgroundOperation();
					mainHandler_.post(new Runnable() {

						@Override
						public void run() {
							helper_.updateUserInterfaceWithResultFromNetwork();
						}
					});
					Looper.myLooper().quit();
					break;
				default:
					break;
				}
			}
		};

		Looper.loop();
	}
}