package Test;
import java.io.FileNotFoundException;
import java.io.IOException;

import data.Dataset;
import diagrammes.Diagrammes;
import input_sources.FileManager;
import input_sources.URLManager;

public class Test_dataset {
	public static void main(String[] args) throws Exception {
		Dataset dataset = FileManager.extract_dataset(null);
		System.out.println(dataset.proba_instance(21, 2));
	}
	
	/* utilisez le GUI pour tester les digrammes
	private static void test_histogram() throws FileNotFoundException {
		Dataset dataset = FileManager.extract_dataset(null);
		Diagrammes diag = new Diagrammes(dataset);
		diag.histogram(0);
	}*/
	
	@SuppressWarnings("unused")
	private static void test_dispersion_diag() throws FileNotFoundException {
		Dataset dataset = FileManager.extract_dataset(null);
		Diagrammes diag = new Diagrammes(dataset, null);
		diag.diagramme_disperssion(5, 6);
	}
	
	@SuppressWarnings("unused")
	private static void testMoyenneTronquee() throws FileNotFoundException {
		Dataset dataset = FileManager.extract_dataset(null);
		for (int i = 0; i < 50; i++) {
			System.out.println(dataset.moyenne_tronqee(0,i/100.0));
		}
	}
	
	public static void testMesures() throws Exception {
		Dataset dataset = FileManager.extract_dataset(null);
		System.out.println(dataset.mesures_string());
		// exemple d'utilisation
		dataset.ecartType(0);
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
