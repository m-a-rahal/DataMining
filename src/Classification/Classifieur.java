package Classification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;

import javax.swing.table.TableModel;

import Classification.Classifieur.Classification;
import Classification.Classifieur.Instance;
import Classification.Evaluation.Evaluations;


public abstract class Classifieur {
	int n,m,taille_echantillon_apprentissage;
	public Collection<String> classes_possibles;
	public Classifieur(int n, int m, int taille_echantillon_apprentissage) {
		this.n = n;
		this.m = m;
		this.taille_echantillon_apprentissage = taille_echantillon_apprentissage;
	}

	public Evaluations evaluer(Classification resultats) {
		Evaluations mesures = new Evaluations(resultats);
		for(String classe : resultats.classes) {
			Evaluation m = new Evaluation(resultats, classe);
			mesures.add(m);
		}
		return mesures;
	}
	
	public ArrayList<Instance> instances_de_test(TableModel model) {
		// par défaut les instances sont prises du dataset (table)
		ArrayList<Instance> instances = new ArrayList<>();
		// construire les instances
		Compteur compteur = new Compteur();
		for (int i = 0; i < n; i++) {
			String classe = model.getValueAt(i, m-1).toString();
			compteur.incrementer(classe); // compter la taille de la classe
			if (compteur.getOrDefault(classe,0) > taille_echantillon_apprentissage) {// prendre uniquement les échantillons de test
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

	public static class Instance extends ArrayList<String> {
		private static final long serialVersionUID = 1L;
		public int numero_instance;
		public String classe_predite, classe_correcte;

		public Instance(int numero_instance, String classe_correcte) {
			super();
			this.numero_instance = numero_instance;
			this.classe_correcte = classe_correcte;
		}

		@Override
		public String toString() {
			return "#"+numero_instance+"=\"" + String.join(" ", this)+"\"";
		}
	}

	public static class Classification extends TreeMap<Integer, Instance> {
		private static final long serialVersionUID = 1L;
		public ArrayList<String> classes;

		public Classification(Collection<String> classes) {
			this.classes = new ArrayList<>(classes);
		}

		public void ajouter(Instance instance, String classe) {
			instance.classe_predite = classe;
			put(instance.numero_instance, instance);
		}
		

		@Override
		public String toString() {
			String text = "";
			for (Integer num_instance : this.keySet()) {
				Instance instance = get(num_instance);
				String vrai_classe = instance.classe_correcte != null ? "\t("+instance.classe_correcte+")" : "";
				text += num_instance+ ")\t"+ String.join(" ", instance) +"\t=>\t"+instance.classe_predite + vrai_classe +"\n";
			}
			return text;
		}
	}
	
	public static class Compteur extends HashMap<String, Integer> {
		private static final long serialVersionUID = 1L;
		public void incrementer(String classe) {
			Integer taille = getOrDefault(classe, 0);
			if(taille == null) taille = 0;
			put(classe, taille+1);
		}
	}
	
	public Classification tester(ArrayList<Instance> instances) {
		Classification classifications = new Classification(classes_possibles);
		for(Instance instance : instances) {
			classifications.ajouter(instance, classifier(instance));
		}
		return classifications;
	}

	public abstract String classifier(Instance instance);

	public static ArrayList<Instance> extraire_instances(String text) {
		ArrayList<Instance> instances = new ArrayList<>();
		int i = 1;
		for(String ligne : text.strip().split("\\s*\\n\\s*")) {
			String[] items = ligne.split("[^\\w\\.0-9]+");
			String classe = null;
			if(items.length > 7)
				classe = items[items.length-1];
			Instance instance = new Instance(i++, classe);
			for (int j = 0; j < items.length - 1; j++) {
				instance.add(items[j]);
			}
			instances.add(instance);
		}
		return instances;
	}
}
