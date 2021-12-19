package Algorithmes;
import java.util.TreeSet;

public class Itemsets extends TreeSet<Ensemble<String>>{
	private static final long serialVersionUID = 1L;
	public IDsets iDsets; // utilisé dans l'algorithme ECLAT
	
	@Override
	public String toString() {
		String text = "{ ";
		for (Ensemble<String> itemset : this) {
			text += itemset.toString() + " ";
		}
		return text + "}";
	}
	
	// Pour l'algrorithme ECLAT
	public Ensemble<Integer> ids(Ensemble<String> itemset) {
		return iDsets.get(itemset.key());
	}
}