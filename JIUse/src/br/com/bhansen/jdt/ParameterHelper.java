package br.com.bhansen.jdt;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.JavaModelException;

public class ParameterHelper {

	final static Set<String> primitives = new HashSet<>();
	final static Set<String> collections = new HashSet<>();

	static {
		
		primitives.add("Object");
		primitives.add("byte");
		primitives.add("Byte"); 
		primitives.add("short");
		primitives.add("Short");
		primitives.add("int");
		primitives.add("Integer");
		primitives.add("long");
		primitives.add("Long");
		primitives.add("float");
		primitives.add("Float");
		primitives.add("double");
		primitives.add("Double");
		primitives.add("char");
		primitives.add("Character");
		primitives.add("boolean");
		primitives.add("Boolean");
		primitives.add("String");
		primitives.add("void");		

		collections.add("Collection");
		collections.add("Set");
		collections.add("List");
		collections.add("Queue");
		collections.add("Deque");
		collections.add("Map");
		collections.add("SortedSet");
		collections.add("SortedMap");
		collections.add("NavigableSet");
		collections.add("NavigableMap");
		collections.add("BlockingQueue");
		collections.add("TransferQueue");
		collections.add("BlockingDeque");
		collections.add("ConcurrentMap");
		collections.add("ConcurrentNavigableMap");
		collections.add("HashSet");
		collections.add("TreeSet");
		collections.add("LinkedHashSet");
		collections.add("ArrayList");
		collections.add("ArrayDeque");
		collections.add("LinkedList");
		collections.add("PriorityQueue");
		collections.add("HashMap");
		collections.add("TreeMap");
		collections.add("LinkedHashMap");
		
	}

	protected final static String explodGenerics(String parameters) {
		String generics = parameters;
		
		generics = generics.replaceAll("\\s", " ");// Change whitespace character: [\t\n\x0B\f\r]
		generics = generics.replaceAll(",", ", ");// Add space after comma
		generics = generics.replaceAll(" {2,}", " ");// Remove more than one
		
		generics = generics.replaceAll("[\\?|\\w+] super ", "");
		generics = generics.replaceAll("[\\?|\\w+] extends ", "");
		generics = generics.replaceAll("<", ", ");
		generics = generics.replaceAll(">", "");
		generics = generics.replaceAll(", \\?", "");
		
		generics = generics.replaceAll(" {2,}", " ");// Remove more than one
		generics = generics.replaceAll(" {1,},", ",");// Remove before comma
		
		return generics;
	}
	
	public final static boolean isPrimitive(String type) {
		return primitives.contains(type);
	}

	public final static void removePrimitives(Set<String> parameters) {
		parameters.removeAll(primitives);
	}
	
	public final static void removeCollections(Set<String> parameters) {
		parameters.removeAll(collections);
	}
		
	public static final void removeSelfParameter(Set<String> parameters, String className) throws JavaModelException {
	
		parameters.remove(Signature.normalizeInnerSignature(className));
		parameters.remove(Signature.normalizeSignature(className));
		return;
	
	}
}
