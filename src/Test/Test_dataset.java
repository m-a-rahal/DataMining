package Test;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.sound.midi.Soundbank;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import Classification_baysienne.ClassifieurBaysien;
import data.Dataset;
import diagrammes.Diagrammes;
import input_output_classes.FileManager;
import input_output_classes.URLManager;
import motifs_frequents_et_regles.Apriori;
import motifs_frequents_et_regles.Eclat;
import motifs_frequents_et_regles.Itemsets;
import motifs_frequents_et_regles.Regle;

public class Test_dataset {
	public static void main(String[] args) throws Exception {
		Dataset dataset = FileManager.extract_dataset(null);
		dataset.normaliser_min_max();
		dataset.discretiser_equal_width(4);
		int n,m;
		n = dataset.n;
		m = dataset.m;
		TableModel model = model_from_table(dataset);
		discretiser(model);
		ClassifieurBaysien classifieur = new ClassifieurBaysien(model,n,m);
		System.out.println(classifieur.p_laplace("I12", "1.0"));
	}
	
	@SuppressWarnings("unused")
	private static void test_motifs_frequents() throws FileNotFoundException {
		Dataset dataset = FileManager.extract_dataset(null);
		Itemsets L = test_Eclat(dataset, 0.03, "resources/Market_Basket_Optimisation.csv");
		test_Apriori(dataset, 0.44, "resources/exemple_TD_2.txt");
		//System.out.println(dataset.proba_instance(21, 2));
		test_RegleAssociation_corr(L,0.2);
	}
	
	private static void test_RegleAssociation_corr(Itemsets L, double min_conf) {
		System.out.println("-- règles association -----------------------------------------------------------------");
		System.out.println(Regle.regles_association(L, min_conf));
		System.out.println("-- règles association positivement corrélées ------------------------------------------");
		System.out.println(Regle.regles_correlation(L, min_conf, +1));
		System.out.println("-- règles association négativement corrélées ------------------------------------------");
		System.out.println(Regle.regles_correlation(L, min_conf, -1));
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
	
	private static TableModel model_from_table(Dataset dataset) throws Exception {
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
	
	private static void discretiser(TableModel model) {
		for (int j = 0; j < model.getColumnCount()-2; j++) {
			for (int i = 0; i < model.getRowCount(); i++) {
				int k = (int) Math.floor((double) model.getValueAt(i, j));
				model.setValueAt("I"+(j+1)+""+k, i, j);
			}
		}
	}
}
