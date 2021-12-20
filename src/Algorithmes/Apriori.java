package Algorithmes;
import javax.swing.table.TableModel;

import input_output_classes.PatternFileManager;

public class Apriori extends AlgoMotifsFrequents {
	
	
	public Apriori(TableModel dataset, double min_sup_pourcent, double min_conf_pourcent) {
		super(dataset, min_sup_pourcent, min_conf_pourcent);
	}
	public Apriori(TableModel dataset, double min_sup_pourcent, double min_conf_pourcent, boolean inclure_attrib_classe) {
		super(dataset, min_sup_pourcent, min_conf_pourcent, inclure_attrib_classe);
	}
	
	public Itemsets run(String file_path) {
		file_manager = new PatternFileManager(file_path);
		// associer a chaque element un support
		Supports supports = file_manager.extraire_les_1_itmesets();
		// extraitre la liste des items (L1)
		int min_sup = min_sup();
		Itemsets L = new Itemsets(); // L = L1 dans cette étape
		for (String item : supports.keySet()) {
			if (supports.get(item) >= min_sup) {
				L.add(new Ensemble<String>(item));
			};
		}
		
		int k = 2;
		Itemsets Lk = new Itemsets();
		Lk.addAll(L);
		// construire la liste L2
		while (!Lk.isEmpty()) {
			Itemsets Lk_1 = combiner_itmesets(Lk);
			file_manager.calculer_supports(Lk_1);
			//System.out.println(Lk_1.last());
			Lk = new Itemsets();
			for (Ensemble<String> itemset : Lk_1) {
				if (itemset.support >= min_sup) {
					Lk.add(itemset);
				}
			}
			L.addAll(Lk);
		}
		
		return L;
	}

}
