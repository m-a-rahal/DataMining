package motifs_frequents_et_regles;

import java.util.HashMap;

public class Supports extends HashMap<String, Integer>{
	public void ajouter(String item) {
		// compter le support d'un element
		Integer sup = get(item);
		if (sup == null) {
			put(item, 1);
		} else {
			put(item, sup+1);
		}
	}
	
	public void ajouter(Ensemble<String> itemset) {
		put(itemset.key(), itemset.support);
	}
	
	public Integer get(Ensemble<String> obj) {
		return get(obj.key());
	}
}