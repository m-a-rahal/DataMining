package Algorithmes;
import input_output_classes.PatternFileManager;

public class Apriori extends AlgoMotifsFrequents {

	
	public Apriori(double min_sup_pourcent) {
		super(min_sup_pourcent);
	}

	public Itemsets run(String file_path) {
		file_manager = new PatternFileManager(file_path);
		// associer a chaque element un support
		Supports supports = file_manager.extraire_les_1_itmesets(this);
		// extraitre la liste des items (L1)
		Itemsets L = new Itemsets(); // L = L1 dans cette étape
		for (String item : supports.keySet()) {
			if (supports.get(item) >= min_sup()) {
				Ensemble<String> itemset = new Ensemble<String>(item);
				itemset.support = supports.get(item);
				L.add(itemset);
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
				if (itemset.support >= min_sup()) {
					Lk.add(itemset);
				}
			}
			L.addAll(Lk);
		}
		
		L.nbr_totale_instances = nbr_instances;
		return L;
	}

}
