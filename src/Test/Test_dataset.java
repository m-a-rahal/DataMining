package Test;
import java.io.FileNotFoundException;
import java.io.IOException;

import data.Dataset;
import diagrammes.Diagrammes;
import input_sources.FileManager;
import input_sources.URLManager;
import mesures.Mesures;

public class Test_dataset {
	public static void main(String[] args) throws Exception {
		testMesures();
	}
	
	private static void test_dispersion_diag() throws FileNotFoundException {
		Dataset dataset = FileManager.extract_dataset(null);
		Diagrammes diag = new Diagrammes(dataset);
		diag.diagramme_disperssion(5, 6);
	}
	
	public static void testMesures() throws Exception {
		Dataset dataset = FileManager.extract_dataset(null);
		Mesures mesures = new Mesures(dataset);
		System.out.println(mesures);
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
