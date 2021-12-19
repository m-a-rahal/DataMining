package Algorithmes;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.table.TableModel;

import Algorithmes.Itemsets.Itemset;

public abstract class AlgoMotifsFrequents {
	public TableModel dataset;
	public double min_sup_pourcent;
	public double min_conf_pourcent;
	public int nbr_lignes, nbr_colonnes; // dimension du dataset;
	public static final String dataset_file = "_tmp_dataset.csv";
	PatternFileManager file_manager;
	
	public AlgoMotifsFrequents(TableModel dataset, double min_sup_pourcent, double min_conf_pourcent) {
		this.dataset = dataset;
		this.min_sup_pourcent = min_sup_pourcent;
		this.min_conf_pourcent = min_conf_pourcent; 
		nbr_lignes = dataset.getRowCount();
		nbr_colonnes = dataset.getColumnCount()-1; // inclure uniquement les données du dataset
		file_manager = new PatternFileManager(this);
	}

	public AlgoMotifsFrequents(TableModel dataset, double min_sup_pourcent, double min_conf_pourcent, boolean inclure_attrib_classe) {
		this(dataset, min_sup_pourcent, min_conf_pourcent);
		if (!inclure_attrib_classe)
			nbr_colonnes = dataset.getColumnCount()-2;
	}
	
	public Itemsets run() {
		file_manager.ecrir_dataset_dans_fichier();
		try {
			return itemsets_frequent();
		} finally {
			file_manager.delete_dataset_file();
		}
	}
	
	public abstract Itemsets itemsets_frequent();
	
	public String item(int i, int j) {
		return dataset.getValueAt(i, j).toString();
	}
	
	protected class PatternFileManager {
		AlgoMotifsFrequents parent;
		BufferedReader reader;
		final String separator = ",";

		
		protected PatternFileManager(AlgoMotifsFrequents parent) {
			this.parent = parent;
		}
		
		protected void ecrir_dataset_dans_fichier() {
			try {
				PrintWriter writer = new PrintWriter(dataset_file);
				for (int i=0; i< nbr_lignes; i++) {
					for (int j=0; j< nbr_colonnes; j++) {
						writer.print(item(i, j));
						if (j<nbr_colonnes-1) writer.print(separator); 
					}
					if (i<nbr_lignes-1) writer.print("\n");
				}
				writer.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		protected void delete_dataset_file() {
			try {
				Files.delete(Paths.get(dataset_file));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		protected void calculer_supports(Itemsets Lk) {
			try {
				
				this.start_reader();
				String line = next_line();
				while(line != null) {
					Itemset pattern = new Itemset(line.split(separator));
					for (Itemset itemset : Lk) {
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
	}
	
	public int min_sup() {
		return (int) Math.ceil(min_sup_pourcent*dataset.getRowCount());
	}
}
