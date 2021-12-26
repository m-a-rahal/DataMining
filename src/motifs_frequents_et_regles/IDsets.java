package motifs_frequents_et_regles;

import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;

public class IDsets extends HashMap<String, Ensemble<Integer>>{
	private static final long serialVersionUID = 1L;

	public void ajouter(String item, int id) {
		Ensemble<Integer> ids = get(item);
		if (ids == null) {
			ids = new Ensemble<Integer>();
			put(item, ids);
		}
		ids.add(id);
	}

	public int support(String item) {
		return get(item).size();
	}
	
	@Override
	public String toString() {
		String text = "{ ";
		for (String item : this.keySet()) {
			Ensemble<Integer> idset = get(item);
			text += item+ ": "+idset.toString() + "\n";
		}
		return text + "}";
	}
	
}
