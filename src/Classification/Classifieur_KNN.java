package Classification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.table.TableModel;

public class Classifieur_KNN extends Classifieur {
	public static final int Euclidienne = 0, Manhattan = 1;
	public int k;
	ArrayList<Instance> instances;
	int fonction_distance = Euclidienne;
	
	public Classifieur_KNN(TableModel model, int n, int m, int k, int taille_echantillon_apprentissage) {
		super(n, m, taille_echantillon_apprentissage);
		this.k = k;
		apprendre(model);
	}
	
	public void utiliser_distance_manathan() {
		fonction_distance = Manhattan;
	}
	public void utiliser_distance_euclidienne() {
		fonction_distance = Euclidienne;
	}
	
	public void apprendre(TableModel model) {
		TreeSet<String> classes = new TreeSet<String>();
		instances = new ArrayList<>();
		Compteur compteur = new Compteur();
		for (int i = 0; i < n; i++) {
			String classe = model.getValueAt(i, m-1).toString();
			if (compteur.getOrDefault(classe,0) < taille_echantillon_apprentissage) {
				compteur.incrementer(classe); // compter la taille de la classe
				classes.add(classe);
				Instance instance =  new Instance(i+1, classe);
				for (int j = 0; j < m-1; j++) {
					String x = model.getValueAt(i, j).toString();
					instance.add(x);
				}
				instances.add(instance);
			}
		}
		this.classes_possibles = new ArrayList<>(classes);
	}
	
	public String classifier(Instance instance) {
		// calculer distances
		ArrayList<Distance> distances = new ArrayList<>();
		int max = 0;
		for (int i = 0; i < instances.size(); i++) {
			distances.add(new Distance(distance(instance, instances.get(i)), instances.get(i)));
		}
		Collections.sort(distances);
		List<Distance> best = distances.subList(0, k);
		TreeMap<String, Integer> votes = new TreeMap<>();
		for(Distance dist : best) {
			votes.put(dist.instance.classe_correcte, 1 + votes.getOrDefault(dist.instance.classe_correcte, 0));
		}
		int best_vote = Collections.max(votes.values());
		
		ArrayList<String> classifications = new ArrayList<>();
		for(String classe : votes.keySet()) {
			if(votes.get(classe) == best_vote) {
				classifications.add(classe);
			}
		}
		if(classifications.size() == 1) return classifications.get(0);
		else return "{"+String.join(",", classifications)+"}";
	}
	
	public Double distance(Instance instance, Instance instance2) {
		double dist = 0.0;
		double tmp;
		if(fonction_distance == Euclidienne) {
			for (int i = 0; i < instance.size(); i++) {
				tmp = distance_locale(instance, instance2, i);
				tmp *= tmp;
				dist += tmp;
			}
			dist = Math.sqrt(dist);
		} else if (fonction_distance == Manhattan) {
			for (int i = 0; i < instance.size(); i++) {
				tmp = distance_locale(instance, instance2, i);
				dist += Math.abs(tmp);
			}
		}
		return dist;
	}
	
	public Double distance_locale(Instance instance, Instance instance2, int i) {
		double dist = 0.0;
		try {
			dist = Double.parseDouble(instance.get(i)) - Double.parseDouble(instance2.get(i));
			return dist;
		} catch (Exception e) {
			try {
				String x = instance.get(i); x = (x+" ").substring(2, x.length());
				String y = instance2.get(i); y = (y+" ").substring(2, y.length());
				dist = Double.parseDouble(x) - Double.parseDouble(y);
			} catch (Exception e2) {
				e2.printStackTrace();
				return Double.NEGATIVE_INFINITY;
			}
		}
		return dist;
	}

	
	
	public class Distance implements Comparable<Distance>{
		public Double distance;
		public Instance instance;
		public Distance(Double distance, Instance instance) {
			super();
			this.distance = distance;
			this.instance = instance;
		}

		@Override
		public int compareTo(Distance other) {
			return this.distance.compareTo(other.distance);
		}
		
	}
}
