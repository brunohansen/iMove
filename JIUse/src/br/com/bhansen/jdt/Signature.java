package br.com.bhansen.jdt;

import java.util.regex.Pattern;

public class Signature {
	
	final protected static Pattern accessorPattern = Pattern.compile("^(get|set|is)[A-Z|0-9|_|$]{1,}");

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

}
