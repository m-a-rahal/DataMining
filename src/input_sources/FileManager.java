package input_sources;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import data.Dataset;

public class FileManager {
	public static String default_dataset_file = "resources/seeds_dataset.txt";
	
	public static Dataset extract_dataset(String dataset_file) throws FileNotFoundException {
		if (dataset_file==null) dataset_file = default_dataset_file;
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(dataset_file)));
		
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
