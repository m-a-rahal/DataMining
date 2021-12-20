package Test;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import Algorithmes.Apriori;
import Algorithmes.Eclat;
import data.Dataset;
import diagrammes.Diagrammes;
import input_output_classes.FileManager;
import input_output_classes.URLManager;

public class Test_dataset {
	public static void main(String[] args) throws Exception {
		Dataset dataset = FileManager.extract_dataset(null);
		test_Eclat(dataset, 0.44, 0.6, false, "resources/exemple_TD.txt");
		test_Apriori(dataset, 0.44, 0.6, false, "resources/exemple_TD.txt");
		//System.out.println(dataset.proba_instance(21, 2));
		
	}
	
	private static void test_Eclat(Dataset dataset,double min_sup_pourcent, double min_conf_pourcent, boolean inclure_attrib_classe, String file) {
		try {
			Eclat eclat = new Eclat(min_sup_pourcent, min_conf_pourcent,inclure_attrib_classe);
			System.out.println(eclat.run(file));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void test_Apriori(Dataset dataset, double min_sup_pourcent, double min_conf_pourcent, boolean inclure_attrib_classe, String file) {
		try {
			Apriori apriori = new Apriori(min_sup_pourcent, min_conf_pourcent,inclure_attrib_classe);
			System.out.println(apriori.run(file));
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
	
	private static TableModel load_dataset_on_table(Dataset dataset) throws Exception {
		/** charger la dataset dans la table et afficher les mesures
		 * */
		
		String[] col_names_with_number = new String[dataset.col_names.length+1]; for (int i = 0; i < dataset.col_names.length; i++) {col_names_with_number[i]=dataset.col_names[i];}; col_names_with_number[dataset.col_names.length] = "#";
		
		// load table in Jtabel
		TableModel tableModel = new DefaultTableModel(col_names_with_number, dataset.n);
		// afficher les données originales
		for (int i = 0; i < dataset.n; i++) {
			for (int j = 0; j < dataset.m; j++) {
				tableModel.setValueAt(dataset.data[i][j], i, j);
			}
		}
		return tableModel;
	}
}
