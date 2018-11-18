package br.com.bhansen.refactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.ui.IWorkbenchWindow;

import com.google.common.collect.Sets;

import br.com.bhansen.metric.iuc.IUC;
import br.com.bhansen.metric.iuc.IUCClass;

public class ExtractClass {

	public ExtractClass(IWorkbenchWindow window, IUCClass iucClass, String methodSignature, double metricLowerLimit, int mthdsLowerLimit) {

		List<Map<String, Set<String>>> classes = removeDuplicates(createSuperClasses(iucClass, metricLowerLimit, mthdsLowerLimit));

		classes.forEach(e -> System.out.println(IUC.toString("Class" + classes.size(), IUCClass.getMetric(e), e)));

	}

	public List<Map<String, Set<String>>> createSuperClasses(IUCClass iucClass, double metricValue, int mthdNumber) {
		List<Entry<String, Set<String>>> mostCalled = sort(iucClass.getMethods(), (o1, o2) -> new Integer(o2.getValue().size()).compareTo(o1.getValue().size()));

		for (Entry<String, Set<String>> method : mostCalled) {
			List<Map<String, Set<String>>> classes = createSuperClassesForMethod(iucClass, method.getKey(), metricValue, mthdNumber);

			if (! classes.isEmpty())
				return classes;
		}

		return new ArrayList<>();
	}

	public List<Map<String, Set<String>>> createSuperClassesForMethod(IUCClass iucClass, String methodSignature, double metricLowerLimit, int mthdsLowerLimit) {
		
		Set<Map<String, Set<String>>> classes = new HashSet<>();
		
		double metricValue = 1.0;
		
		Map<String, Set<String>> methods = iucClass.getMethodsWithoutThis();
		
		//Permite extrair a super classe mais coesa possivel que atenda o número de métodos
		while (classes.size() == 0 && metricValue >= metricLowerLimit) {
			
			classes = createClassesForMethod(methods, methodSignature, metricValue, mthdsLowerLimit);

			metricValue = decreseMetricValue(metricValue, "0.1");
		}
		
		return sortByNumCallers(classes);
	}

	public List<Map<String, Set<String>>> createClassesForMethod(IUCClass iucClass, String methodSignature, double metricLowerLimit, int mthdsLowerLimit) {
		Map<String, Set<String>> methods = iucClass.getMethodsWithoutThis();
		
		//Permite extrair a maior classe que atenda a metrica e o numero de metodos
		Set<Map<String, Set<String>>> classes = createClassesForMethod(methods, methodSignature, metricLowerLimit, mthdsLowerLimit);

		return sortByNumMethods(classes);
	}
	
	private Set<Map<String, Set<String>>> createClassesForMethod(Map<String, Set<String>> methods, String methodSignature, double metricLowerLimit, int mthdsLowerLimit) {
		Map<Set<String>, Set<String>> groupedMethods = IUC.groupMethodsByCallers(methods);
		Set<String> callersGroup = methods.get(methodSignature);
		Set<String> methodsGroup = groupedMethods.remove(callersGroup);

		Set<Map<String, Set<String>>> classes = new HashSet<>();

		// Generate Powerset using Guava
		Set<Set<Set<String>>> result = Sets.powerSet(groupedMethods.keySet());

		for (Set<Set<String>> combCallers : result) {
			Map<String, Set<String>> classs = new HashMap<>();

			for (String m : methodsGroup) {
				classs.put(m, callersGroup);
			}

			for (Set<String> callers : combCallers) {
				for (String method : groupedMethods.get(callers)) {
					classs.put(method, callers);
				}
			}

			if (classs.size() >= mthdsLowerLimit) {
				double metric = IUCClass.getMetric(classs);

				if (metric >= metricLowerLimit) {
					classes.add(classs);
				}
			}
		}

		return classes;
	}
	
	public List<Map<String, Set<String>>> createClassesForCallers(IUCClass iucClass, double metricLowerLimit, int mthdsLowerLimit) {
		Map<String, Set<String>> methods = iucClass.getMethodsWithoutThis();
		Map<String, Map<String, Set<String>>> callersMethods = IUC.getMethodsByCaller(methods);

		Set<Map<String, Set<String>>> classes = new HashSet<>();

		for (Entry<String, Map<String, Set<String>>> caller : callersMethods.entrySet()) {
			Set<Map<String, Set<String>>> result = createClasses(caller.getValue(), metricLowerLimit, mthdsLowerLimit);
			
			if (! result.isEmpty()) {
				classes.addAll(result);
			}
		}

		return sortByNumMethods(classes);
	}

	public List<Map<String, Set<String>>> createClasses(IUCClass iucClass, double metricLowerLimit, int mthdsLowerLimit) {
		Map<String, Set<String>> methods = iucClass.getMethodsWithoutThis();
		
		return sortByNumMethods(createClasses(methods, metricLowerLimit, mthdsLowerLimit));
	}
	
	private Set<Map<String, Set<String>>> createClasses(Map<String, Set<String>> methods, double metricLowerLimit, int mthdsLowerLimit) {
		Map<Set<String>, Set<String>> groupedMethods = IUC.groupMethodsByCallers(methods);
		Set<Map<String, Set<String>>> classes = new HashSet<>();

		// Generate Powerset using Guava
		Set<Set<Set<String>>> result = Sets.powerSet(groupedMethods.keySet());

		for (Set<Set<String>> combCallers : result) {

			Map<String, Set<String>> classs = new HashMap<>();

			for (Set<String> callers : combCallers) {
				for (String method : groupedMethods.get(callers)) {
					classs.put(method, callers);
				}
			}

			if (classs.size() >= mthdsLowerLimit) {
				double metric = IUCClass.getMetric(classs);

				if (metric >= metricLowerLimit) {
					classes.add(classs);
				}
			}
		}

		return classes;
	}

	private Set<Map<String, Set<String>>> createClassesForCaller(Map<String, Set<String>> methods, double metricLowerLimit, int mthdsLowerLimit) {
		Set<Map<String, Set<String>>> classes = new HashSet<>();

		if (methods.size() >= mthdsLowerLimit) {
			
			if (IUCClass.getMetric(methods) >= metricLowerLimit) {
				classes.add(methods);				
			} else {
				Map<String, Map<String, Set<String>>> callersMethods = IUC.getMethodsByCaller(methods);

				for (Entry<String, Map<String, Set<String>>> caller : callersMethods.entrySet()) {
					if (!caller.getValue().equals(methods)) {
						Set<Map<String, Set<String>>> result = createClassesForCaller(caller.getValue(), metricLowerLimit, mthdsLowerLimit);

						if (! result.isEmpty()) {
							classes.addAll(result);
						}
					}
				}
			}
		}
		
		return classes;
	}
	
	private double decreseMetricValue(double metricValue, String step) {
		return new BigDecimal(String.valueOf(metricValue)).subtract(new BigDecimal(step)).doubleValue();
	}

	private List<Map<String, Set<String>>> removeDuplicates(List<Map<String, Set<String>>> list) {
		List<Map<String, Set<String>>> result = new ArrayList<>();

		for (Map<String, Set<String>> l : list) {
			boolean add = true;

			for (Map<String, Set<String>> r : result) {
				if (r.keySet().containsAll(l.keySet())) {
					add = false;
					break;
				}
			}

			if (add) {
				result.add(l);
			}
		}

		return result;
	}

	private List<Map<String, Set<String>>> sortByNumCallers(Collection<Map<String, Set<String>>> collection) {
		return sort(collection, new ComparatorByNumCallers());
	}

	private class ComparatorByNumCallers implements Comparator<Map<String, Set<String>>> {

		@Override
		public int compare(Map<String, Set<String>> o1, Map<String, Set<String>> o2) {
			List<Entry<String, Set<String>>> o1L = sortBySize(o1);
			List<Entry<String, Set<String>>> o2L = sortBySize(o2);

			int result = 0;

			for (int i = 0; i < o2L.size() && i < o1L.size(); i++) {
				result = new Integer(o2L.get(i).getValue().size()).compareTo(o1L.get(i).getValue().size());

				if (result != 0)
					return result;
			}

			result = new Integer(o2.size()).compareTo(o1.size());

			if (result != 0)
				return result;

			return new Double(IUCClass.getMetric(o2)).compareTo(IUCClass.getMetric(o1));
		}

	}

	private List<Map<String, Set<String>>> sortByNumMethods(Collection<Map<String, Set<String>>> collection) {
		return sort(collection, new ComparatorByNumMethods());
	}
	
	private class ComparatorByNumMethods implements Comparator<Map<String, Set<String>>> {

		@Override
		public int compare(Map<String, Set<String>> o1, Map<String, Set<String>> o2) {
			int result = new Integer(o2.size()).compareTo(o1.size());

			if (result != 0)
				return result;

			return new Double(IUCClass.getMetric(o2)).compareTo(IUCClass.getMetric(o1));
		}

	}
	
	private <K, V extends Collection<?>> List<Entry<K, V>> sortBySize(Map<K, V> collection) {
		return sort(collection, (o1, o2) -> new Integer(o2.getValue().size()).compareTo(o1.getValue().size()));
	}
	
	private <K, V> List<Entry<K, V>> sort(Map<K, V> map, Comparator<? super Entry<K, V>> comparator) {
		return sort(map.entrySet(), comparator);
	}

	private <T> List<T> sort(Collection<T> collection, Comparator<? super T> comparator) {
		List<T> list = new ArrayList<>(collection);

		Collections.sort(list, comparator);

		return list;
	}

}
