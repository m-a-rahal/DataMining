package Classification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import Classification.Evaluation.Evaluations;


public class Classifieur {
	
	public Evaluations evaluer(Classification resultats) {
		Evaluations mesures = new Evaluations(resultats);
		for(String classe : resultats.classes) {
			Evaluation m = new Evaluation(resultats, classe);
			mesures.add(m);
		}
		return mesures;
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
}
