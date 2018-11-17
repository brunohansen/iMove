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

		List<Map<String, Set<String>>> classes = createSuperClassesForMethod(iucClass, methodSignature, metricLowerLimit, mthdsLowerLimit);
		
		classes.forEach(e -> System.out.println(IUC.toString("Class" + classes.size(), IUCClass.getMetric(e), e)));
		
	}

	private double decreseMetricValue(double metricValue, String step) {
		return new BigDecimal(String.valueOf(metricValue)).subtract(new BigDecimal(step)).doubleValue();
	}
	
	public List<Map<String, Set<String>>> createSuperClasses(IUCClass iucClass, double metricValue, int mthdNumber) {
		List<Entry<String, Set<String>>> mostCalled = sort(iucClass.getMethods(), (o1, o2) -> new Integer(o2.getValue().size()).compareTo(o1.getValue().size()));
		
		for (Entry<String, Set<String>> method : mostCalled) {
			List<Map<String, Set<String>>> classes = createClassesForMethod(iucClass, method.getKey(), metricValue, mthdNumber);
			
			if(classes.size() > 0)
				return sortByNumCallers(classes);
		}
		
		return new ArrayList<>();
	}
	
	private <K, V> List<Entry<K, V>> sort(Map<K, V> map, Comparator<? super Entry<K, V>> comparator) {
		return sort(map.entrySet(), comparator);
	}
	
	private <K, V extends Collection<?>> List<Entry<K, V>> sortBySize(Map<K, V> collection) {
		return sort(collection, (o1, o2) -> new Integer(o2.getValue().size()).compareTo(o1.getValue().size()));
	}
	
	private List<Map<String, Set<String>>> sortByNumCallers(Collection<Map<String, Set<String>>> collection) {
		return sort(collection, new Comparator<Map<String, Set<String>>>() {

			@Override
			public int compare(Map<String, Set<String>> o1, Map<String, Set<String>> o2) {
				List<Entry<String, Set<String>>> o1L = sortBySize(o1);
				List<Entry<String, Set<String>>> o2L = sortBySize(o2);
				
				for (int i = 0; i < o2L.size() && i < o1L.size(); i++) {
					if(o2L.get(i).getValue().size() != o1L.get(i).getValue().size())
						return new Integer(o2L.get(i).getValue().size()).compareTo(o1L.get(i).getValue().size());
				}
				
				return new Integer(o2.size()).compareTo(o1.size());
			}
		});
	}
	
	private List<Map<String, Set<String>>> sortByIUC(Collection<Map<String, Set<String>>> collection) {
		return sort(collection, (o1, o2) -> new Double(IUCClass.getMetric(o2)).compareTo(IUCClass.getMetric(o1)));
	}
	
	private <T> List<T> sort(Collection<T> collection, Comparator<? super T> comparator) {
		List<T> list = new ArrayList<>(collection);
		
		Collections.sort(list, comparator);
		
		return list;
	}
	
	public List<Map<String, Set<String>>> createSuperClassesForMethod(IUCClass iucClass, String methodSignature, double metricLowerLimit, int mthdsLowerLimit) {
		return sortByNumCallers(createClassesForMethod(iucClass, methodSignature, metricLowerLimit, mthdsLowerLimit));
	}

	public List<Map<String, Set<String>>> createClassesForMethod(IUCClass iucClass, String methodSignature, double metricLowerLimit, int mthdsLowerLimit) {
		
		Map<String, Set<String>> methods = iucClass.getMethodsWithoutThis();
		Map<Set<String>, Set<String>> groupedMethods = IUC.groupMethodsByCallers(methods);
		Set<String> callersGroup = methods.get(methodSignature);
		Set<String> methodsGroup = groupedMethods.remove(callersGroup);

		Set<Map<String, Set<String>>> classes = new HashSet<>();

		// Generate Powerset using Guava
		Set<Set<Set<String>>> result = Sets.powerSet(groupedMethods.keySet());
		
		double metricValue = 1.0;

		while(classes.size() == 0 && metricValue >= metricLowerLimit) {
			
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

					if (metric >= metricValue) {
						classes.add(classs);
					}
				}
			}
			
			metricValue = decreseMetricValue(metricValue, "0.1");			
		}

		return sortByIUC(classes);
	}

	public List<Map<String, Set<String>>> createClasses(Map<Set<String>, Set<String>> groupedMethods, double metricValue, int mthdNumber) {

		Set<Map<String, Set<String>>> classes = new HashSet<>();

		// Generate Powerset using Guava
		Set<Set<Set<String>>> result = Sets.powerSet(groupedMethods.keySet());

		// print results
		for (Set<Set<String>> combCallers : result) {

			Map<String, Set<String>> classs = new HashMap<>();

			for (Set<String> callers : combCallers) {
				for (String method : groupedMethods.get(callers)) {
					classs.put(method, callers);
				}
			}

			if (classs.size() >= mthdNumber) {
				double metric = IUCClass.getMetric(classs);

				if (metric >= metricValue) {
					classes.add(classs);
				}
			}
		}

		return sortByIUC(classes);
	}

}
