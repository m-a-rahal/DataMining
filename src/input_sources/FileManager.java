package input_sources;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import data.Dataset;

public class FileManager {
	public static String default_dataset_file = "resources/seeds_dataset.txt";
	
	public static Dataset extract_dataset(String dataset_file) throws FileNotFoundException {
		if (dataset_file==null) dataset_file = default_dataset_file;
		
		BufferedReader reader = new BufferedReader(new FileReader(dataset_file));
		
			String line;
			try {
				ArrayList<String> datalines = new ArrayList<>();
				while (true) {
					line = reader.readLine();if (line == null) break;
					datalines.add(line);
				}
				reader.close();
				return new Dataset(null, datalines);
				
			} catch (IOException e) {
				try {
					reader.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
			return null;
		
	}
}
