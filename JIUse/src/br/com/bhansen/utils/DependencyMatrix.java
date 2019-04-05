package br.com.bhansen.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

import br.com.bhansen.jdt.Method;
import br.com.bhansen.jdt.ParameterHelper;
import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.Metric;
import br.com.bhansen.metric.MetricFactory;
import br.com.bhansen.refactory.EvaluatorFactory;

public class DependencyMatrix {
	
	private List<String> columns;
	private List<String> rows;
	private Map<String, String> methods;
	private Type type;

	private boolean[][] matrix;

	public DependencyMatrix(Type _type, String _metric, IProgressMonitor monitor) throws Exception {
		MetricFactory factoryL = EvaluatorFactory.createMetricFactory("method", _metric);
		Map<String, List<String>> methodsL = new TreeMap<String, List<String>>();
		
		Set<String> columnsL = new HashSet<>();
		
		methods = new HashMap<>();
		
		type = _type;
		
		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
		
		Map<String, Set<String>> ms = factoryL.create(type, subMonitor.split(5)).getMethods();
		
		subMonitor = SubMonitor.convert(subMonitor.split(95), ms.size());

		for (Entry<String, Set<String>> m : ms.entrySet()) {
			
			Metric metric = factoryL.create(type, m.getKey(), subMonitor.split(1));
			double correlation = metric.getMetric();
			
			Set<String> values = m.getValue();
			columnsL.addAll(values);			
			String strValues = convertValues(values);
			
			Method mtd = type.getMethod(m.getKey());
			
			String key = ( 1 - correlation + strValues) + " " + mtd.getVisibility() + mtd.getName();
			
			methods.put(key, m.getKey());
			
			methodsL.put(key, sort(values));
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

	public List<String> getRows() {
		return rows;
	}

	public Method getMethod(String row) throws Exception {
		return type.getMethod(methods.get(row));
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
	
	public interface Visitor {
		
		public void visit(List<String> columns, List<String> rows, Map<String, String> methods, Type type, boolean[][] matrix);
	}
	
	public void visit(Visitor visitor) {
		visitor.visit(columns, rows, methods, type, matrix);
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
					builder.append("\t & \\rot{" + Type.getAbbreviatedName(column) + "}");
				}
								
				builder.append("\\\\");
				builder.append("\n\t\t\\cmidrule{2-" + (columns.size() + 2) + "}");
				builder.append("\n\t\\rowcolor{black!10} \\cellcolor{white}");
				
				int x = 0;
				for (String method : rows) {
					builder.append("\n\t\t & " + Method.getAbbreviatedName(method.substring(method.lastIndexOf(" ") + 1)));
					
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
