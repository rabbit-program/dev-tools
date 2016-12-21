package com.king.linux.util;

public class ThreadUtil {
	
	public static void execute(Runnable runnable) {
		Thread thread = new Thread(runnable);
		thread.setDaemon(true);
		thread.start();
	}
	
}

