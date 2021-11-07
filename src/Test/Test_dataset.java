package Test;
import java.io.FileNotFoundException;
import java.io.IOException;

import data.Dataset;
import input_sources.FileManager;
import input_sources.URLManager;
import mesures.Mesures;

public class Test_dataset {
	public static void main(String[] args) throws Exception {
		testMesures();
	}
	
	public static void testMesures() throws Exception {
		Dataset dataset = FileManager.extract_dataset(null);
		Mesures mesures = new Mesures(dataset);
		System.out.println(mesures.moyenne(0));
		System.out.println(mesures.ecartType(0));
		System.out.println(mesures.mediane(0));
	}
	
	public static void testFile_extraction() throws IOException {
		Dataset dataset = FileManager.extract_dataset(null);
		dataset.show();
	}
	
	public static void testURL_extraction() throws IOException {
		Dataset dataset = URLManager.extract_dataset(null);
		dataset.show();
	}
}
