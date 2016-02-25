package com.huang.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.util.Log;

public class Flog {

	public static String TAG = "huangjinqing";
	public static boolean DEBUG = true;
	public static boolean FILE_LOGCAT = false;
	public static final boolean SCREEN_DEBUG = false;
	public static final boolean INFO = true;

	public static void e(String Tag, String msg) {
		if (INFO)
			Log.e(TAG + "---" + Tag, msg);
	}

	public static void e(String msg) {
		if (INFO)
			Log.e(TAG, msg);
	}

	public static void d(String Tag, String msg) {
		if (DEBUG)
			Log.d(TAG + "---" + Tag, msg);
	}

	public static void d(String msg) {
		if (DEBUG)
			Log.d(TAG, msg);
	}

	public static void i(String msg) {
		if (INFO)
			Log.i(TAG, msg);
	}

	private boolean isRun = false;
	private void execCmd(String cmd) throws IOException {
		Runtime runtime = Runtime.getRuntime();
		Process process = runtime.exec(cmd);
		InputStream is = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line = null;
		while (null != (line = br.readLine())) {
			Log.e("Flog", line);
		}
	}

}