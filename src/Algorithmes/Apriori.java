package Algorithmes;
import java.util.ArrayList;
import javax.swing.table.TableModel;
import Algorithmes.Itemsets.Itemset;

public class Apriori extends AlgoMotifsFrequents {
	
	
	public Apriori(TableModel dataset, double min_sup_pourcent, double min_conf_pourcent) {
		super(dataset, min_sup_pourcent, min_conf_pourcent);
	}
	public Apriori(TableModel dataset, double min_sup_pourcent, double min_conf_pourcent, boolean inclure_attrib_classe) {
		super(dataset, min_sup_pourcent, min_conf_pourcent, inclure_attrib_classe);
	}
	
	public Itemsets run() {
		file_manager.ecrir_dataset_dans_fichier();
		try {
			return run_apriori();
		} finally {
			file_manager.delete_dataset_file();
		}
	}

	public Itemsets run_apriori() {
		// associer a chaque element un support
		Supports supports = file_manager.extraire_les_1_itmesets();
		// extraitre la liste des items (L1)
		int min_sup = min_sup();
		Itemsets L = new Itemsets(); // L = L1 dans cette étape
		for (String item : supports.keySet()) {
			if (supports.get(item) >= min_sup) {
				L.add(new Itemset(item));
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
			for (Itemset itemset : Lk_1) {
				if (itemset.support >= min_sup) {
					Lk.add(itemset);
				}
			}
			L.addAll(Lk);
		}
		
		return L;
	}
	
	private Itemsets combiner_itmesets(Itemsets Lk) {
		Itemsets Lk1 = new Itemsets();
		ArrayList<Itemset> itemsets = new ArrayList<Itemset>(Lk);
		for (int i = 0; i < itemsets.size(); i++) {
			Itemset A = itemsets.get(i);
			for (int j = i+1; j < itemsets.size(); j++) {
				Itemset B = itemsets.get(j);
				if(A.size() == 1 || A.intersect(B).size() == 1) {
					Lk1.add(A.union(B));
				}
			}
		}
		return Lk1;
	}

	public int min_sup() {
		return (int) Math.ceil(min_sup_pourcent*dataset.getRowCount());
	}
}
