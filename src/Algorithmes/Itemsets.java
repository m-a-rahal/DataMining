package Algorithmes;
import java.util.TreeSet;

public class Itemsets extends TreeSet<Itemsets.Itemset>{
	private static final long serialVersionUID = 1L;
	
	public static class Itemset extends TreeSet<String> {
		private static final long serialVersionUID = 1L;

		public Itemset(String item) {
			super();
			this.add(item);
		}
		
		@Override
		public String toString() {
			String text = "{";
			for (String item : this) {
				text += item + " ";
			}
			return text + "}";
		}
	}
	
	@Override
	public String toString() {
		String text = "{";
		for (Itemset itemset : this) {
			text += itemset.toString() + " ";
		}
		return text + "}";
	}
}