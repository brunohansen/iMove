package br.com.bhansen.utils;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

public class DependencyMatrix {

	private Set<String> columns;
	private Set<String> rows;

	private boolean[][] matrix;

	public DependencyMatrix(Map<String, Set<String>> methods) {
		columns = new TreeSet<>();
		rows = new TreeSet<>();

		for (Entry<String, Set<String>> method : methods.entrySet()) {
			rows.add(method.getKey());
			columns.addAll(method.getValue());
		}

		matrix = new boolean[rows.size()][columns.size()];

		int x = 0;
		for (String method : rows) {
			Set<String> values = methods.get(method);

			int y = 0;
			for (String value : columns) {
				matrix[x][y] = values.contains(value);
				y++;
			}

			x++;
		}

	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

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
					builder.append("\n\t\t & " + method.split("\\(", 2)[0]);

					int y = 0;
					for (String value : columns) {
						if(matrix[x][y]) {
							builder.append("\t & \\OK");
						} else {
							builder.append("\t & ");
						}
						
						y++;
					}

					x++;
					builder.append("\t\\\\");
				}
				builder.append("\n\t\t\\rot{\\rlap{~Methods}}");
				builder.append("\n\t\t\\cmidrule{2-" + (columns.size() + 2) + "}");
				builder.append("\n\t\\end{tabular}");
			builder.append("\n\t\\caption{Some caption}");
		builder.append("\n\\end{table}");

		return builder.toString();
	}

}
