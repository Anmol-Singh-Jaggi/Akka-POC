package com.anmol.util;

public class CommonUtil {
	public static void waitForTime(Integer seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
