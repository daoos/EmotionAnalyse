package com.ibm.emotion.util;

import com.ibm.emotion.constant.Constants;

public class LogUtil {

	public static void printlog(String log) {
		if (Constants.DEBUG) {
			System.out.println(log);
		}
	}
}
