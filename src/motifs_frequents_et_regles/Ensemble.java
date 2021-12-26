package motifs_frequents_et_regles;

import java.util.ArrayList;
import java.util.TreeSet;

public class Ensemble<V> extends TreeSet<V> implements Comparable<Ensemble>{
	private static final long serialVersionUID = 1L;
	public int support = 0;

	public Ensemble(Ensemble<V> ensemble) {
		super(ensemble);
	}
	
	public Ensemble(V item) {
		super();
		this.add(item);
	}
	
	public Ensemble(V[] items) {
		super();
		for (int i = 0; i < items.length; i++) {
			add(items[i]);
		}
	}


	public Ensemble() {
		super();
	}

	public Ensemble<V> union(Ensemble<V> other) {
		Ensemble<V> union = new Ensemble<V>(this);
		union.addAll(other);
		return union;
	}
	
	public Ensemble<V> intersect(Ensemble<V> other) {
		Ensemble<V> inter = new Ensemble<V>(this);
		inter.retainAll(other);
		return inter;
	}
	
	public Ensemble<V> substract(Ensemble<V> other) {
		Ensemble<V> sub = new Ensemble<V>(this);
		sub.removeAll(other);
		return sub;
	}
	
	public ArrayList<Ensemble<V>> sousEnsembles(){
		ArrayList<Ensemble<V>> sousEnsembles = new ArrayList<Ensemble<V>>();
		ArrayList<V> this_ens = new ArrayList<V>(this);
		int n = size();
		for (int i = 0; i < (1<<n); i++){
			Ensemble<V> current = new Ensemble<V>();
			for (int j = 0; j < n; j++)
				if ((i & (1 << j)) > 0)
					current.add(this_ens.get(j));
			if(!current.isEmpty() && current.size() != n)
				sousEnsembles.add(current);
		}
		return sousEnsembles;
	}
	
	@Override
	public String toString() {
		return "{" + String.join(",", (Iterable<? extends CharSequence>) this) + "}:"+support;
	}
	
	public String toString_simple() { // ne pas afficher support
		return "{" + String.join(",", (Iterable<? extends CharSequence>) this) + "}";
	}

	@Override
	public int compareTo(Ensemble other) {
		// TODO Auto-generated method stub
		return this.key().compareTo(other.key());
	}
	
	public String key() {
		return String.join(" ", (Iterable<? extends CharSequence>) this);
	}
}
