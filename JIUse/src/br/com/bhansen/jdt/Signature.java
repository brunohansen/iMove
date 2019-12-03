package br.com.bhansen.jdt;

import java.util.regex.Pattern;

public class Signature {
	
	final protected static Pattern accessorPattern = Pattern.compile("^(get|set|is)[A-Z|0-9|_|\\$]{1,}");

	public static String normalizeSignature(String signature) {
		signature = normalizeSpaces(signature);
		signature = signature.replaceAll("[a-z|A-Z|0-9|_|$]*?\\.", "");// Remove packages and inner classes
	
		return signature;
	}

	public static String normalizeInnerSignature(String signature) {
		signature = normalizeSpaces(signature);
		signature = signature.replaceAll("([a-z|0-9|_|$]*\\.){2,}", "");// Remove just packages
	
		return signature;
	}
	
	public static String normalizeSpaces(String signature) {
		signature = signature.replaceAll("\\s", " ");// Change whitespace character: [\t\n\x0B\f\r]
		signature = signature.replaceAll(",", ", ");// Add space after comma
		signature = signature.replaceAll(" {2,}", " ");// Remove more than one
	
		return signature;
	}
	
	public static boolean isAccessorMethod(String signature) {
		return accessorPattern.matcher(signature).find();
	}
	
	public static boolean isSetterOrGetterFor(String signature, Type type) {
		if(signature.equals("get" + type.getSimpleName() + "():" + type.getSimpleName())) return true;
		if(signature.equals("set" + type.getSimpleName() + "(" + type.getSimpleName() + "):void")) return true;
		
		return isSetterOrGetter(signature);
	}
	
	public static boolean isSetterOrGetter(String signature) {		
		if(signature.matches("^get[a-z|A-Z|0-9|_|\\$]+\\(\\):Date$")) return true;
		if(signature.matches("^set[a-z|A-Z|0-9|_|\\$]+\\(Date\\):void$")) return true;
		
		for (String primitive : ParameterHelper.primitives) {
			if(signature.matches("^get[a-z|A-Z|0-9|_|\\$]+\\(\\):" + primitive + "$")) return true;
			if(signature.matches("^set[a-z|A-Z|0-9|_|\\$]+\\(" + primitive + "\\):void$")) return true;
		}
		
		return false;
	}

}
