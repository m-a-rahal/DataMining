package Algorithmes;
import java.util.ArrayList;
import javax.swing.table.TableModel;

import input_output_classes.PatternFileManager;

public class Eclat extends AlgoMotifsFrequents{

	public Eclat(double min_sup_pourcent) {
		super(min_sup_pourcent);
	}

	public Itemsets run(String file_path) {
		file_manager = new PatternFileManager(file_path);
		
		// extraitre la liste des items (L1)
		Itemsets L = new Itemsets(); // L = L1 dans cette étape
		// calculer les IDsets des items du dataset
		L.iDsets = file_manager.extraire_IDs_des_items(this);
		for (String item : L.iDsets.keySet()) {
			if (L.iDsets.support(item) >= min_sup()) {
				Ensemble<String> itemset = new Ensemble<String>(item);
				itemset.support = L.iDsets.support(item);
				L.add(itemset);
			};
		}
		
		Itemsets Lk = new Itemsets();
		Lk.addAll(L);
		Lk.iDsets = L.iDsets;
		// construire la liste L2
		while (!Lk.isEmpty()) {
			Itemsets Lk_1 = combiner_itmesets(Lk);
			//System.out.println(Lk_1.last());
			Lk = new Itemsets();
			Lk.iDsets = Lk_1.iDsets;
			for (Ensemble<String> itemset : Lk_1) {
				int sup = Lk_1.iDsets.support(itemset.key());
				if (sup >= min_sup()) {
					itemset.support = sup;
					Lk.add(itemset);
				}
			}
			L.addAll(Lk);
		}
		
		L.iDsets = null; // libérer l'espac mémoire
		L.nbr_totale_instances = nbr_instances;
		return L;
	}
	
	@Override
	protected Itemsets combiner_itmesets(Itemsets Lk) {
		Itemsets Lk1 = new Itemsets();
		Lk1.iDsets = new IDsets();
		ArrayList<Ensemble<String>> itemsets = new ArrayList<Ensemble<String>>(Lk);
		for (int i = 0; i < itemsets.size(); i++) {
			Ensemble<String> A = itemsets.get(i);
			for (int j = i+1; j < itemsets.size(); j++) {
				Ensemble<String> B = itemsets.get(j);
				Ensemble<String> union = A.union(B);
				if(union.size() == A.size() + 1) { // if it only adds one new elements
					Lk1.add(union);
					Lk1.iDsets.put(union.key(), Lk.iDsets.get(A.key()).intersect(Lk.iDsets.get(B.key())));
				}
			}
		}
		return Lk1;
	}

}
