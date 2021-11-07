package data;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FileManager {
	public static String default_dataset_file = "resources/seeds_dataset.txt";
	
	public static Dataset extract_dataset(String file_name) throws FileNotFoundException {
		if (file_name==null) file_name = default_dataset_file;
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file_name)));
		
			String line;
			try {
				String names[] = "area,perimeter,compactness,length of kernel,width of kernel,asymmetry coefficient,length of kernel groove".split(",");
				ArrayList<String> datalines = new ArrayList<>();
				while (true) {
					line = reader.readLine();if (line == null) break;
					datalines.add(line);
				}
				return new Dataset(names, datalines);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		
	}
}
