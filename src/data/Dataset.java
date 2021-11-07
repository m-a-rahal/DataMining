package data;

import java.util.HashMap;
import java.util.List;

public class Dataset {
	public double data [][] = null;
	public int n; // dataset dimentions, lines and columns repectively
	public int m;
	HashMap<String, Integer> col_index; // used to get column index by column name
	String col_names [];
	
	public Dataset(String[] names, List<String> data_lines) {
		// extract data from string lines
		extract_data(data_lines);
		// init col_index
		int col = 0;
		col_index = new HashMap<>();
		col_names = names;
		for (String col_name : names) {
			col_index.put(col_name, col++);
		}
	}
	
	private void extract_data(List<String> data_lines) {
		/** extracts rows from space-separated lines (Strings)
		 * eg: 1.5 1.45 156.2 --> [1.5, 1.45, 156.2]
		 * */
		// detect data dimensions
		n = data_lines.size();
		m = data_lines.get(0).split("[\s\t]+").length;
		// allocate data matrix
		this.data = new double[n][m];
		// fill data matrix with values
		int i = 0;
		for (String line : data_lines) {
			int j = 0;
			for (String str_value : line.split("[\s\t]+")) {
				data[i][j++] = Double.parseDouble(str_value);
			}i++;
		}
		
	}

	public double get(int line, String col_name) {
		int col = col_index.get(col_name);
		return get(line, col);
	}
	
	public double get(int line, int col) {
		return data[line][col];
	}

	public void show() {
		for (String name : col_names) {
			System.out.print(name + "\t");
		}
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				System.out.print(data[i][j] + "\t");
			}
			System.out.println(""); // line break
		}
	}

}
