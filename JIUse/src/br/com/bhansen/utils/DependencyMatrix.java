package br.com.bhansen.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.eclipse.core.runtime.NullProgressMonitor;

import br.com.bhansen.metric.MetricFactory;
import br.com.bhansen.refactory.EvaluatorFactory;

public class DependencyMatrix {
	
	private List<String> columns;
	private List<String> rows;

	private boolean[][] matrix;

	public DependencyMatrix(Type _type, String _metric, Map<String, Set<String>> _methods, Map<String, String> _methodVisibilities) throws Exception {
		MetricFactory factoryL = EvaluatorFactory.createMetricFactory("method", _metric);
		Map<String, List<String>> methodsL = new TreeMap<String, List<String>>();
		
		Set<String> columnsL = new HashSet<>();
		
		for (Entry<String, Set<String>> method : _methods.entrySet()) {
			
			columnsL.addAll(method.getValue());
			
			double correlation = factoryL.create(_type, method.getKey(), new NullProgressMonitor()).getMetric();
			String values = convertValues(method.getValue());
			String visibility = _methodVisibilities.get(method.getKey());
			String methodName = method.getKey().split("\\(", 2)[0];
			
			methodsL.put(( 1 - correlation + values) + " " + visibility + methodName, sort(method.getValue()));
		}
		
		columns = sort(columnsL);
		
		rows = new ArrayList<>(methodsL.keySet());
		
//		for (String r : new ArrayList<>(rows)) {
//			if(r.split(" ").length == 2) {
//				rows.remove(r);
//				rows.add(r);
//			}
//		}
		
		matrix = new boolean[rows.size()][columns.size()];

		int x = 0;
		for (String method : rows) {
			List<String> values = methodsL.get(method);

			int y = 0;
			for (String value : columns) {
				matrix[x][y] = values.contains(value);
				y++;
			}

			x++;
		}

	}
	
	private String convertValues(Set<String> _values) {
		Set<String> valuesL = new TreeSet<>();
		
		for (String value : _values) {
			valuesL.add(value.substring(value.lastIndexOf('.') + 1));
		}
		
		String values = "";
		
		for (String value : valuesL) {
			values = values + " " + value;
		}
		
		return values;		
	}
	
	private List<String> sort(Set<String> _columns) {
		Set<String> objects = new TreeSet<>();
		Set<String> primitives = new TreeSet<>();
		
		for (String value : _columns) {
			if(ParameterHelper.isPrimitive(value)) {
				primitives.add(value.substring(value.lastIndexOf('.') + 1));
			} else {
				objects.add(value.substring(value.lastIndexOf('.') + 1));
			}
		}
		
		ArrayList<String> sorted = new ArrayList<>(objects);
		sorted.addAll(primitives);
		
		return sorted;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		int mIndex = (rows.size() / 2) + 1;

		builder.append("\\begin{table} \\centering");
			builder.append("\n\t\\begin{tabular}{@{} cl*{" + columns.size() + "}c @{}}");
				builder.append("\n\t\t& & \\multicolumn{" + columns.size() + "}{c}{Values} \\\\[2ex]");
				builder.append("\n\t\t&");
				
				for (String column : columns) {
					builder.append("\t & \\rot{" + column + "}");
				}
								
				builder.append("\\\\");
				builder.append("\n\t\t\\cmidrule{2-" + (columns.size() + 2) + "}");
				builder.append("\n\t\\rowcolor{black!10} \\cellcolor{white}");
				
				int x = 0;
				for (String method : rows) {
					builder.append("\n\t\t & " + method.substring(method.lastIndexOf(" ") + 1));
					
					for (int y = 0; y < columns.size(); y++) {
						if(matrix[x][y]) {
							builder.append("\t & \\OK");
						} else {
							builder.append("\t & ");
						}
					}
					
					builder.append("\t\\\\");
					
					if(x == mIndex) {
						builder.append("\n\t\t\\rot{\\rlap{~Methods}}");
					}

					x++;
				}
				builder.append("\n\t\t\\cmidrule{2-" + (columns.size() + 2) + "}");
				builder.append("\n\t\\end{tabular}");
			builder.append("\n\t\\caption{Some caption}");
		builder.append("\n\\end{table}");

		return builder.toString();
	}

}
