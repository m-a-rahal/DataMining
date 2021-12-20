package Test;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import Algorithmes.AlgoMotifsFrequents;
import Algorithmes.Apriori;
import Algorithmes.Eclat;
import data.Dataset;
import diagrammes.Diagrammes;
import input_output_classes.FileManager;
import input_output_classes.URLManager;

public class Test_dataset {
	public static void main(String[] args) throws Exception {
		Dataset dataset = FileManager.extract_dataset(null);
		test_Eclat(dataset, 0.2, 0.6, false);
		test_Apriori(dataset, 0.2, 0.6, false);
		//System.out.println(dataset.proba_instance(21, 2));
		
	}
	
	private static void test_Eclat(Dataset dataset,double min_sup_pourcent, double min_conf_pourcent, boolean inclure_attrib_classe) {
		try {
			dataset.normaliser_min_max();
			dataset.discretiser_equal_width(4);
			TableModel model = load_dataset_on_table(dataset);
			Eclat eclat = new Eclat(model, min_sup_pourcent, min_conf_pourcent,inclure_attrib_classe);
			discretiser(eclat);
			System.out.println(eclat.run("resources/dataset_discret.txt"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void test_Apriori(Dataset dataset, double min_sup_pourcent, double min_conf_pourcent, boolean inclure_attrib_classe) {
		try {
			dataset.normaliser_min_max();
			dataset.discretiser_equal_width(4);
			TableModel model = load_dataset_on_table(dataset);
			Apriori apriori = new Apriori(model, min_sup_pourcent, min_conf_pourcent,inclure_attrib_classe);
			discretiser(apriori);
			System.out.println(apriori.run("resources/dataset_discret.txt"));
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
	
	private static void discretiser(AlgoMotifsFrequents algo) {
		TableModel model = algo.dataset;
		for (int j = 0; j < algo.nbr_colonnes; j++) {
			for (int i = 0; i < algo.nbr_lignes; i++) {
				int k = (int) Math.floor((double) model.getValueAt(i, j));
				model.setValueAt("I"+(j+1)+""+k, i, j);
			}
		}
	}
}
