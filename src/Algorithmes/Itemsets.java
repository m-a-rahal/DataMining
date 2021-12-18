package Algorithmes;
import java.util.TreeSet;

public class Itemsets extends TreeSet<Itemsets.Itemset>{
	private static final long serialVersionUID = 1L;
	
	public static class Itemset extends TreeSet<String> implements Comparable<Itemset>{
		private static final long serialVersionUID = 1L;
		public int support;

		public Itemset(String item) {
			super();
			this.add(item);
		}
		
		public Itemset(Itemset itemset) {
			super(itemset);
		}

		public Itemset(String[] items) {
			super();
			for (int i = 0; i < items.length; i++) {
				add(items[i]);
			}
		}

		public Itemset union(Itemset other) {
			Itemset union = new Itemset(this);
			union.addAll(other);
			return union;
		}
		
		public Itemset intersect(Itemset other) {
			Itemset inter = new Itemset(this);
			inter.retainAll(other);
			return inter;
		}
		
		@Override
		public String toString() {
			String text = "{ ";
			for (String item : this) {
				text += item + " ";
			}
			return text + "}";
		}

		@Override
		public int compareTo(Itemset other) {
			// TODO Auto-generated method stub
			return ((Integer)this.hashCode()).compareTo(other.hashCode());
		}
	}
	
	@Override
	public String toString() {
		String text = "{ ";
		for (Itemset itemset : this) {
			text += itemset.toString() + " ";
		}
		return text + "}";
	}
}