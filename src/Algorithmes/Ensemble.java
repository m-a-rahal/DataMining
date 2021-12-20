package Algorithmes;

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
	
	@Override
	public String toString() {
		return "{" + String.join(",", (Iterable<? extends CharSequence>) this) + "}:"+support;
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
