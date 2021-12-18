package Algorithmes;

import javax.swing.table.TableModel;

import Algorithmes.Itemsets.Itemset;

public class Apriori {
	TableModel dataset;
	double min_sup_pourcent;
	double min_conf_pourcent;
	int nbr_lignes, nbr_colonnes; // dimension du dataset;
	
	public Apriori(TableModel dataset, double min_sup_pourcent, double min_conf_pourcent) {
		this.dataset = dataset;
		this.min_sup_pourcent = min_conf_pourcent;
		this.min_conf_pourcent = min_conf_pourcent; 
		nbr_lignes = dataset.getRowCount();
		nbr_colonnes = dataset.getColumnCount()-1; // inclure uniquement les données du dataset
	}
	
	public void run() {
		// associer a chaque element un support
		Supports supports = new Supports();
		for (int i = 0; i < nbr_lignes; i++) {
			for (int j = 0; j < nbr_colonnes; j++) {
				supports.ajouter(item(i, j)); // le support d'un item = 0 par defaut
			}
		}
		// extraitre la liste des items (L1)
		int min_sup = min_sup();
		Itemsets L = new Itemsets();
		for (String item : supports.keySet()) {
			if (supports.get(item) > min_sup) {
				L.add(new Itemsets.Itemset(item));
			}
		}
	}
	
	public int min_sup() {
		return (int) Math.ceil(min_sup_pourcent*dataset.getRowCount());
	}
	
	public String item(int i, int j) {
		return (String) dataset.getValueAt(i, i);
	}
	
}
