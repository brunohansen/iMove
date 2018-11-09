package br.com.bhansen.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import br.com.bhansen.metric.iuc.IUC;
import br.com.bhansen.metric.iuc.IUCClass;

public class SetUtils {

	public static Map<String, CompositeSet> splitByMaxPairIntersection1(Map<String, Set<String>> map) {
		Map<String, CompositeSet> classes = splitByMaxPairIntersection(SingleSet.from(map));
		int prev = 0;

		while (classes.size() > 2 && classes.size() != prev) {
			prev = classes.size();
			classes = splitByMaxPairIntersection(classes);
		}

		for (Entry<String, CompositeSet> e : classes.entrySet()) {
			System.out.println(e.getValue().toString(e.getKey()));
		}

		return classes;
	}

	public static Map<String, CompositeSet> splitByMaxPairIntersection2(Map<String, Set<String>> map) {
		int CLASS_NUMBER = 2;
		Map<String, CompositeSet> extractedClasses = new HashMap<>();
		Map<String, CompositeSet> classes = splitByMaxPairIntersection(SingleSet.from(map));
		int prev = 0;
		int classNumber = CLASS_NUMBER;

		while (true) {

			// Extraio 2 classes ou até elas não terem relação
			while (classes.size() > classNumber && classes.size() != prev) {
				prev = classes.size();
				classes = splitByMaxPairIntersection(classes);
			}

			// Extraio classes com metodos que ninguem chama e que tenha um iuc
			// maior que o desejado

			Map<String, CompositeSet> classesCopy = new HashMap<>(classes);
			prev = classes.size();

			for (Entry<String, CompositeSet> e : classesCopy.entrySet()) {

				if (e.getValue().getSet().isEmpty()) {
					extractedClasses.put("Class" + extractedClasses.size(), classes.remove(e.getKey()));
				} else if (IUCClass.getMetric(e.getValue().getMap()) >= 0.3 && e.getValue().map.size() >= 4) {
					classNumber = CLASS_NUMBER;
					extractedClasses.put("Class" + extractedClasses.size(), classes.remove(e.getKey()));
				}
			}
			
			if(classes.size() == 0) {
				break;
			}

			if (classes.size() == prev) {
				classNumber++;
			} 
			
			prev = 0;
			Map<String, SetWrapper> cs = new HashMap<>();

			for (Entry<String, CompositeSet> e : classes.entrySet()) {
				cs.putAll(e.getValue().map);
			}
			
			if(cs.size() < classNumber) {
				for (Entry<String, CompositeSet> e : classes.entrySet()) {
					extractedClasses.put("Class" + extractedClasses.size(), classes.get(e.getKey()));
				}
				break;
			}

			classes = splitByMaxPairIntersection(cs);
		}

		for (Entry<String, CompositeSet> e : extractedClasses.entrySet()) {
			System.out.println(e.getValue().toString(e.getKey()));
		}

		return classes;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, CompositeSet> splitByMaxPairIntersection(Map<String, ? extends SetWrapper> map) {

		Map<String, SetWrapper> mapCopy = new HashMap<>(map);
		Map<String, CompositeSet> classes = new HashMap<>();

		// Retiro quem não tem nada e crio uma classe para eles
		CompositeSet compositeSet = new CompositeSet();

		for (Entry<String, ? extends SetWrapper> e : map.entrySet()) {

			if (e.getValue().getSet().isEmpty()) {
				compositeSet.put(e.getKey(), mapCopy.remove(e.getKey()));
			}

		}

		if (!compositeSet.isEmpty()) {
			classes.put("Class" + classes.size(), compositeSet);
		}

		// Verifico os pares mais semelhantes
		Entry<String, SetWrapper>[] entries = mapCopy.entrySet().toArray(new Entry[1]);

		Map<String, Double> result = new HashMap<>();

		for (int x = 0; x < entries.length; x++) {

			for (int y = 0; y < entries.length; y++) {

				if (x == y) {
					result.put(entries[x].getKey() + " - " + entries[y].getKey(), -1.0);
				} else {
					result.put(entries[x].getKey() + " - " + entries[y].getKey(),
							SetUtils.howMuchIntersect(entries[x].getValue().getSet(), entries[y].getValue().getSet()));
				}
			}
		}

		List<Entry<String, Double>> resultList = new ArrayList<Entry<String, Double>>(result.entrySet());
		Collections.sort(resultList, new Comparator<Map.Entry<String, Double>>() {
			public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});
		
		extractFirst(mapCopy, classes, resultList);		
		//extractAll(mapCopy, classes, resultList);

		// Adiciono quem ainda não tem par
		for (Entry<String, SetWrapper> e : mapCopy.entrySet()) {
			classes.put("Class" + classes.size(), new CompositeSet(e.getKey(), e.getValue()));
		}

		return classes;
	}
	
	private static void extractFirst(Map<String, SetWrapper> mapCopy, Map<String, CompositeSet> classes, List<Entry<String, Double>> resultList) {
		Entry<String, Double> r = resultList.get(0);
		
		// Acabaram as semelhanças
		if (r.getValue() <= 0) {
			return;
		}

		// Cria classes para mais semelhantes e retiro dos não utilizados
		String[] m = r.getKey().split(" - ");

		if (mapCopy.containsKey(m[0]) && mapCopy.containsKey(m[1])) {
			classes.put("Class" + classes.size(),
					new CompositeSet(m[0], mapCopy.remove(m[0]), m[1], mapCopy.remove(m[1])));
		}
	}

	private static void extractAll(Map<String, SetWrapper> mapCopy, Map<String, CompositeSet> classes, List<Entry<String, Double>> resultList) {
		for (Entry<String, Double> r : resultList) {

			// Acabaram as semelhanças
			if (r.getValue() <= 0) {
				break;
			}

			// Cria classes para mais semelhantes e retiro dos não utilizados
			String[] m = r.getKey().split(" - ");

			if (mapCopy.containsKey(m[0]) && mapCopy.containsKey(m[1])) {
				classes.put("Class" + classes.size(), new CompositeSet(m[0], mapCopy.remove(m[0]), m[1], mapCopy.remove(m[1])));
			}
		}
	}

	public static double howMuchIntersect(Set<String> s1, Set<String> s2) {
		Set<String> intersection = new HashSet<>(s1);
		intersection.retainAll(s2);

		Set<String> union = new HashSet<>(s1);
		union.addAll(s2);

		if (union.size() == 0) {
			return 0;
		} else {
			return (double) intersection.size() / (double) union.size();
		}
	}

}

interface SetWrapper {

	public Set<String> getSet();

}

class SingleSet implements SetWrapper {

	private Set<String> set;

	public SingleSet(Set<String> set) {
		super();
		this.set = set;
	}

	@Override
	public Set<String> getSet() {
		return set;
	}

	public static Map<String, SetWrapper> from(Map<String, Set<String>> map) {
		Map<String, SetWrapper> result = new HashMap<>();

		for (Entry<String, Set<String>> e : map.entrySet()) {
			result.put(e.getKey(), new SingleSet(e.getValue()));
		}

		return result;
	}
}

class CompositeSet implements SetWrapper {

	protected Map<String, SetWrapper> map;

	public CompositeSet() {
		this.map = new HashMap<>();
	}

	public CompositeSet(String k, SetWrapper v) {
		this();

		put(k, v);
	}

	public CompositeSet(Map<String, SetWrapper> map) {
		this();

		for (Entry<String, SetWrapper> e : map.entrySet()) {
			put(e.getKey(), e.getValue());
		}

	}

	public boolean isEmpty() {
		return this.map.isEmpty();
	}

	public void put(String k, SetWrapper v) {
		if (v instanceof CompositeSet) {
			this.map.putAll(((CompositeSet) v).map);
		} else {
			this.map.put(k, v);
		}
	}

	public CompositeSet(String k1, SetWrapper v1, String k2, SetWrapper v2) {
		this(k1, v1);

		put(k2, v2);
	}

	public Map<String, Set<String>> getMap() {
		Map<String, Set<String>> result = new HashMap<>();

		for (Entry<String, SetWrapper> e : this.map.entrySet()) {
			result.put(e.getKey(), e.getValue().getSet());
		}

		return result;
	}

	public String toString(String className) {
		Map<String, Set<String>> methods = getMap();
		return IUC.toString(className, IUCClass.getMetric(methods), methods);
	}

	@Override
	public Set<String> getSet() {

		Set<String> result = new HashSet<>();

		for (Entry<String, SetWrapper> e : map.entrySet()) {
			result.addAll(e.getValue().getSet());
		}

		return result;
	}

}
