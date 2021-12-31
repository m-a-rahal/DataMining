package Classification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.table.TableModel;


public class ClassifieurBaysien extends Classifieur {
	public Index index;
	public int n,m;
	private int taille_echantillon_apprentissage;
	public ClassifieurBaysien(TableModel model, int n, int m, int taille_echantillon_apprentissage) {
		this.n = n; this.m = m;
		this.taille_echantillon_apprentissage = taille_echantillon_apprentissage;
		this.index = apprendre(model);
	}

	public double p_classe(String classe) {
		return index.taille(classe)/index.taille_totale();
	}

	public double p(Instance instance, String classe) {
		double p = 1;
		for(String x : instance) {
			p *= p(x, classe);
		}
		return p;
	}

	public double p(String x, String classe) {
		return index.taille(x, classe)/(double)index.taille(classe);
	}

	public double p_laplace(String x, String classe) {
		return (index.taille(x, classe) + 1)/(double)(index.taille(classe) + index.valeurs_possibles.get(x).size());
	}

	private Index apprendre(TableModel model) {
		Index index = new Index(m);
		for (int i = 0; i < n; i++) {
			String classe = model.getValueAt(i, m-1).toString();
			if (index.taille(classe) < taille_echantillon_apprentissage) {
				index.valeurs_possibles.ajouter(m, classe);
				index.incrementer(classe); // compter la taille de la classe
				for (int j = 0; j < m-1; j++) {
					String x = model.getValueAt(i, j).toString();
					index.incrementer(x, classe); // compter la taille de l'attribut dans la classe
				}
			}
		}
		return index;
	}

	public ArrayList<Instance> instances_de_test(TableModel model) {
		// par défaut les instances sont prises du dataset (table)
		ArrayList<Instance> instances = new ArrayList<>();
		// construire les instances
		Index compteur = new Index(m);
		for (int i = 0; i < n; i++) {
			String classe = model.getValueAt(i, m-1).toString();
			compteur.incrementer(classe); // compter la taille de la classe
			if (compteur.taille(classe) > taille_echantillon_apprentissage) {// prendre uniquement les échantillons de test
				Instance instance =  new Instance(i+1, classe);
				for (int j = 0; j < m-1; j++) {
					String x = model.getValueAt(i, j).toString();
					instance.add(x);
				}
				instances.add(instance);
			}
		}
		return instances;
	}

	public Classification tester(ArrayList<Instance> instances) {
		Classification classifications = new Classification(index.valeurs_possibles.get(m));
		for(Instance instance : instances) {
			classifications.ajouter(instance, classifier(instance));
		}
		return classifications;
	}

	private String classifier(Instance instance) {
		HashMap<String, Double> poids = new HashMap<>();
		for(String classe : index.valeurs_possibles.get(m)) {// pour chaque classe
			poids.put(classe, p(instance, classe));
		}
		double max_poids = Collections.max(poids.values());
		ArrayList<String> classifications = new ArrayList<>();
		for(String classe : poids.keySet()) {// pour chaque classe
			if (poids.get(classe) >= max_poids) {
				classifications.add(classe);
			}
		}
		if(classifications.size() == 1) return classifications.get(0);
		else return "{"+String.join(",", classifications)+"}";
	}

	public static class Index extends HashMap<String, Integer> {
		int m;
		public Index(int m) {
			this.m = m;
		}
		public ValeursPossibles valeurs_possibles = new ValeursPossibles();
		private static final long serialVersionUID = 1L;
		public Integer taille(String x, String classe) {return taille(key(x,classe));}
		public double taille_totale() {
			int taille = 0;
			for(String classe : valeurs_possibles.get(m)) {
				taille += taille(classe);
			}
			return taille;
		}
		public Integer taille(String classe) {
			Integer val = super.get(classe);
			if (val == null) return 0;
			else return val;
		}
		public Integer put(String x, String classe, Integer value) {return super.put(key(x,classe), value);}
		public void incrementer(String x, String classe) {
			incrementer(key(x, classe));
			valeurs_possibles.ajouter(x);
		}
		public void incrementer(String classe) {
			Integer taille = taille(classe);
			if(taille == null) taille = 0;
			put(classe, taille+1);
		}
		private String key(String x, String classe) {return x+" "+classe;}
	}
	public static class ValeursPossibles extends HashMap<Integer, HashSet<String>> {
		private static final long serialVersionUID = 1L;

		public void ajouter(String x) {
			int j = attribut_de(x); // détecter l'attribut
			ajouter(j, x);
		}

		public HashSet<String> get(String x) {
			// TODO Auto-generated method stub
			return super.get(attribut_de(x));
		}
		public HashSet<String> get(int attribut) {
			// TODO Auto-generated method stub
			return super.get(attribut);
		}

		public void ajouter(int j, String x) {
			HashSet<String> valeurs = get(j);
			if (valeurs == null) {
				valeurs = new HashSet<>();
				put(j, valeurs);
			}
			valeurs.add(x);
		}

		public Integer attribut_de(String x) {
			return Integer.parseInt(""+x.charAt(1));
		}
	}
}
