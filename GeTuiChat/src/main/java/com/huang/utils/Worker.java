package com.huang.utils;

import android.os.Looper;

public class Worker implements Runnable {

	public static final String TAG = "Worker";
		private final Object mLock = new Object();
		private Looper mLooper;

		/**
		 * Creates a worker thread with the given name. The thread then runs a
		 * {@link Looper}.
		 * 
		 * @param name
		 *            A name for the new thread
		 */
		public Worker(String name) {
			Thread t = new Thread(null, this, name);
			t.setPriority(Thread.NORM_PRIORITY);
			t.start();
			synchronized (mLock) {
				while (mLooper == null) {
					try {
						mLock.wait();
					} catch (InterruptedException ex) {
					}
				}
			}
		}

		public Looper getLooper() {
			return mLooper;
		}

		public void run() {
			synchronized (mLock) {
				Looper.prepare();
				mLooper = Looper.myLooper();
				mLock.notifyAll();
			}
			Looper.loop();
		}

		public void quit() {
			mLooper.quit();
		}
	}
