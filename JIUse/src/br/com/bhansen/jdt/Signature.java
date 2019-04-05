package br.com.bhansen.jdt;

public class Signature {

	public static String normalizeSignature(String signature) {
		signature = signature.replaceAll("\\s", " ");// Change whitespace character: [\t\n\x0B\f\r]
		signature = signature.replaceAll(",", ", ");// Add space after comma
		signature = signature.replaceAll(" {2,}", " ");// Remove more than one
		signature = signature.replaceAll("[a-z|A-Z|0-9|_|$]*?\\.", "");// Remove packages and inner classes
	
		return signature;
	}

	public static String normalizeInnerSignature(String signature) {
		signature = signature.replaceAll("\\s", " ");// Change whitespace character: [\t\n\x0B\f\r]
		signature = signature.replaceAll(",", ", ");// Add space after comma
		signature = signature.replaceAll(" {2,}", " ");// Remove more than one
		signature = signature.replaceAll("([a-z|0-9|_|$]*\\.){2,}", "");// Remove just packages
	
		return signature;
	}

}
