package motifs_frequents_et_regles;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

public class Itemsets extends HashSet<Ensemble<String>>{
	private static final long serialVersionUID = 1L;
	public IDsets iDsets; // utilisé dans l'algorithme ECLAT
	public int nbr_totale_instances;

	@Override
	public String toString() {
		String text = "";
		ArrayList<Ensemble<String>> liste = new ArrayList<>(this);
		liste.sort(new Comparator<Ensemble<String>>() {
			@Override
			public int compare(Ensemble<String> ens1, Ensemble<String> ens2) {
				return ens1.size() - ens2.size();
			}
		});
		for (Ensemble<String> itemset : liste) {
			text += itemset.toString() + "\n";
		}
		return text;
	}

	// Pour l'algrorithme ECLAT
	public Ensemble<Integer> ids(Ensemble<String> itemset) {
		return iDsets.get(itemset.key());
	}
}