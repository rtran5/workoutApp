package com.example.workoutappdemo;


import android.os.Looper;
import android.util.Log;

public class Helper {
	private static String tag = "Concurrency_Exercise";
	
	public void doPotentiallyLongRunningBackgroundOperation() {
		doPotentiallyLongRunningBackgroundOperation("50");
	}

	public void doPotentiallyLongRunningBackgroundOperation(String delay) {
		log("About to request a network resource");

		if (Looper.getMainLooper().getThread() == Thread.currentThread())
			networkOnMain();
		else
			networkOnBack();

		try {
			// Let's simulate a long-running network operation by keeping the
			// current thread busy for a long amount of time
			Thread.sleep(10);
			Log.d(tag, "done background operation "+ delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void updateUserInterfaceWithResultFromNetwork() {
		if (Looper.getMainLooper().getThread() == Thread.currentThread())
			uiOnMain();
		else
			uiOnBack();
	}

	public void log(String message) {
		String threadName = (Looper.getMainLooper().getThread() == Thread
				.currentThread()) ? "main thread" : "background thread";
		Log.i(tag, "[" + threadName + "] " + message);
	}

	// ============================ DO NOT DELETE =======================
	// =
	// =  These are convenience methods that allow Mockito to track behavior
	// =
	public void networkOnMain() {
		log("Incorrect - shouldn't use network on main thread");
	}

	public void networkOnBack() {
		log("Correct - you're performing network operations on background thread. Good work!");
	}
	
	public void uiOnMain() {
		log("This is correct - UI updates go on Main thread. Good work!");
	}

	public void uiOnBack() {
		log("Incorrect - UI updates should only occur on main thread");
	}
}
