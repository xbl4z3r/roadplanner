package me.xbl4z3r.roadplanner.utils;

public class Logger {
	private static final String ERROR_PREFIX = "[RRPATH] [ERROR] ";
	private static final String WARNING_PREFIX = "[RRPATH] [WARNING] ";
	private static final String INFO_PREFIX = "[RRPATH] [INFO] ";
	private static final String DEBUG_PREFIX = "[RRPATH] [DEBUG] ";

	public static void error(String message) {
		System.err.println(ERROR_PREFIX + message);
	}

	public static void warning(String message) {
		System.err.println(WARNING_PREFIX + message);
	}

	public static void info(String message) {
		System.out.println(INFO_PREFIX + message);
	}

	public static void debug(String message) {
		System.out.println(DEBUG_PREFIX + message);
	}

	public static void error(String message, Exception e) {
		System.err.println(ERROR_PREFIX + message);
		e.printStackTrace();
	}

	public static void warning(String message, Exception e) {
		System.err.println(WARNING_PREFIX + message);
		e.printStackTrace();
	}

	public static void info(String message, Exception e) {
		System.out.println(INFO_PREFIX + message);
		e.printStackTrace();
	}

	public static void debug(String message, Exception e) {
		System.out.println(DEBUG_PREFIX + message);
		e.printStackTrace();
	}
}
