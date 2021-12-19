package input_output_classes;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import Algorithmes.AlgoMotifsFrequents;
import Algorithmes.Ensemble;
import Algorithmes.IDsets;
import Algorithmes.Itemsets;
import Algorithmes.Supports;

public class PatternFileManager {
	AlgoMotifsFrequents parent;
	BufferedReader reader;
	final String separator = ",";


	public PatternFileManager(AlgoMotifsFrequents parent) {
		this.parent = parent;
	}
	
	public void ecrir_dataset_dans_fichier() {
		try {
			PrintWriter writer = new PrintWriter(parent.dataset_file);
			for (int i=0; i< parent.nbr_lignes; i++) {
				for (int j=0; j< parent.nbr_colonnes; j++) {
					writer.print(parent.item(i, j));
					if (j<parent.nbr_colonnes-1) writer.print(separator); 
				}
				if (i<parent.nbr_lignes-1) writer.print("\n");
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void delete_dataset_file() {
		try {
			Files.delete(Paths.get(parent.dataset_file));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void calculer_supports(Itemsets Lk) {
		try {
			
			this.start_reader();
			String line = next_line();
			while(line != null) {
				Ensemble<String> pattern = new Ensemble<String>(line.split(separator));
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
			reader = new BufferedReader(new FileReader(parent.dataset_file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private String next_line() throws IOException {
		return reader.readLine();
	}

	public Supports extraire_les_1_itmesets() {
		try {
			this.start_reader();
			String line = next_line();
			Supports supports = new Supports();
			while(line != null) {
				for(String item : line.split(separator)) {
					supports.ajouter(item);
				}
				line = next_line();
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

	public IDsets extraire_IDs_des_items() {
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
