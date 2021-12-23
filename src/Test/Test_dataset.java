package Test;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import Algorithmes.Apriori;
import Algorithmes.Eclat;
import Algorithmes.Itemsets;
import Algorithmes.RegleAssociation;
import data.Dataset;
import diagrammes.Diagrammes;
import input_output_classes.FileManager;
import input_output_classes.URLManager;

public class Test_dataset {
	public static void main(String[] args) throws Exception {
		Dataset dataset = FileManager.extract_dataset(null);
		Itemsets L = test_Eclat(dataset, 0.44, "resources/exemple_TD_2.txt");
		test_Apriori(dataset, 0.44, "resources/exemple_TD_2.txt");
		//System.out.println(dataset.proba_instance(21, 2));
		test_RegleAssociation(L);
	}
	
	private static void test_RegleAssociation(Itemsets L) {
		System.out.println(RegleAssociation.extraitre_regles_association(L, 0.8));
	}

	private static Itemsets test_Eclat(Dataset dataset,double min_sup_pourcent, String file) {
		try {
			Eclat eclat = new Eclat(min_sup_pourcent);
			Itemsets L = eclat.run(file);
			System.out.println(L);
			return L;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static void test_Apriori(Dataset dataset, double min_sup_pourcent, String file) {
		try {
			Apriori apriori = new Apriori(min_sup_pourcent);
			System.out.println(apriori.run(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
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
