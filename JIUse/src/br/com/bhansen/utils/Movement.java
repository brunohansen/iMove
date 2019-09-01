package br.com.bhansen.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Movement {
	
	final protected static Pattern linePattern = Pattern.compile("(([a-z|A-Z|0-9|_|$]*\\.){1,}[a-z|A-Z|0-9|_|$]{1,}::[a-z|A-Z|0-9|_|$]{1,}\\(.*?\\):([a-z|A-Z|0-9|_|$]*\\.)*[a-z|A-Z|0-9|_|$]{1,}(.+[\\]+>+]){0,1}).+?(([a-z|A-Z|0-9|_|$]*\\.){1,}[a-z|A-Z|0-9|_|$]{1,})");
	
	public final static int SOURCE_CLASS = 0;
	public final static int METHOD = 1;
	public final static int TARGET_CLASS = 2;

	public static String[] getMovement(String movement) throws Exception {
		Matcher matcher = linePattern.matcher(movement);
		
		if(matcher.find()) {
			String [] method = matcher.group(1).split("::");
	
			return new String[]{method[0], method[1], matcher.group(5)};
		} else {
			throw new Exception("Invalid syntax: " + movement);
		}
	}
	
	public static String removeMessage(String movement) throws Exception {
		String[] m = getMovement(movement);
		return m[SOURCE_CLASS] + "::" + m[METHOD] + "\t" + m[TARGET_CLASS];
	}

	public static String getSourceClass(String movement) throws Exception {
		return getMovement(movement)[SOURCE_CLASS];
	}
	
	public static String getMethod(String movement) throws Exception {
		return getMovement(movement)[METHOD];
	}
	
	public static String getTargetClass(String movement) throws Exception {
		return getMovement(movement)[TARGET_CLASS];
	}
	
	public static boolean shouldMove(String movement) {
		return movement.split("\t", 2)[0].replaceFirst("(\\d|\\.|-)+-", "").startsWith("0");
	}
	
	public static boolean hasError(String movement) {
		return movement.startsWith("E");
	}
	
	public static String getMessage(String movement) {
		if(movement.contains("Message")) {
			return movement.substring(movement.lastIndexOf("Message: ") + "Message: ".length());
		} else {
			return "There's no message";
		}
	}
}
