package motifs_frequents_et_regles;

import java.util.ArrayList;

import input_output_classes.PatternFileManager;

public abstract class AlgoMotifsFrequents {
	public double min_sup_pourcent;
	public int nbr_instances; // dimension du dataset;
	PatternFileManager file_manager;

	public AlgoMotifsFrequents(double min_sup_pourcent) {
		this.min_sup_pourcent = min_sup_pourcent;
	}

	public abstract Itemsets run(String file_path);

	protected Itemsets combiner_itmesets(Itemsets Lk) {
		Itemsets Lk1 = new Itemsets();
		ArrayList<Ensemble<String>> itemsets = new ArrayList<>(Lk);
		for (int i = 0; i < itemsets.size(); i++) {
			Ensemble<String> A = itemsets.get(i);
			for (int j = i+1; j < itemsets.size(); j++) {
				Ensemble<String> B = itemsets.get(j);
				Ensemble<String> union = A.union(B);
				if(union.size() == A.size() + 1) { // if it only adds one new elements
					Lk1.add(union);
				}
			}
		}
		return Lk1;
	}

	public int min_sup() {
		return (int) Math.ceil(min_sup_pourcent*nbr_instances);
	}
}
