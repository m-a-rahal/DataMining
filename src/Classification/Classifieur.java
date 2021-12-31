package Classification;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;


public class Classifieur {
	
	private ArrayList<Mesures> evaluer(Classification resultats) {
		ArrayList<Mesures> mesures = new ArrayList<>();
		for(String classe : resultats.classes) {
			Mesures m = new Mesures(resultats, classe);
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
		public TreeSet<String> classes = new TreeSet<>();

		public void ajouter(Instance instance, String classe) {
			instance.classe_predite = classe;
			put(instance.numero_instance, instance);
			classes.add(classe);
		}
		
		@Override
		public String toString() {
			String text = "";
			for (Integer num_instance : this.keySet()) {
				Instance instance = get(num_instance);
				String vrai_classe = instance.classe_correcte != null ? " ("+instance.classe_correcte+")" : "";
				text += num_instance+ ") "+ String.join(" ", instance) +" = "+instance.classe_predite + vrai_classe +"\n";
			}
			return text;
		}
	}
}
