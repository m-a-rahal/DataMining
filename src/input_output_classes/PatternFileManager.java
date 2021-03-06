package input_output_classes;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import motifs_frequents_et_regles.Apriori;
import motifs_frequents_et_regles.Eclat;
import motifs_frequents_et_regles.Ensemble;
import motifs_frequents_et_regles.IDsets;
import motifs_frequents_et_regles.Itemsets;
import motifs_frequents_et_regles.Supports;

public class PatternFileManager {
	String dataset_file;
	BufferedReader reader;
	String separator = ",";


	public PatternFileManager(String dataset_file) {
		this.dataset_file = dataset_file;
		separator = FileManager.detect_separator(dataset_file);
	}

	public void calculer_supports(Itemsets Lk) {
		try {

			this.start_reader();
			String line = next_line();
			while(line != null) {
				Ensemble<String> pattern = new Ensemble<>(line.split(separator));
				for (Ensemble<String> itemset : Lk) {
					if (pattern.containsAll(itemset)) {
						itemset.support++;
					}
				}
				line = next_line();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void start_reader() {
		try {
			reader = new BufferedReader(new FileReader(dataset_file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private String next_line() throws IOException {
		return reader.readLine();
	}

	public Supports extraire_les_1_itmesets(Apriori apriori) {
		try {
			this.start_reader();
			String line = next_line();
			Supports supports = new Supports();
			apriori.nbr_instances = 0;
			while(line != null) {
				for(String item : line.split(separator)) {
					supports.ajouter(item);
				}
				line = next_line(); apriori.nbr_instances++;
			}
			return supports;

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public IDsets extraire_IDs_des_items(Eclat eclat) {
		try {
			this.start_reader();
			int id = 0;
			String line = next_line(); id++;
			IDsets iDsets = new IDsets();
			while(line != null) {
				for(String item : line.split(separator)) {
					iDsets.ajouter(item, id);
				}
				line = next_line(); id++;
			}
			eclat.nbr_instances = id-1;
			return iDsets;

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
