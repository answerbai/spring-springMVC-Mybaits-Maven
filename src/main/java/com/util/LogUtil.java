package com.util;

import org.apache.log4j.Logger;

public class LogUtil {
	public final static Logger logger = Logger.getLogger(LogUtil.class);
	public static Logger getLog() {
		return logger;
	}
}
