package Algorithmes;

import java.util.ArrayList;

public class RegleAssociation {
	Ensemble<String> gauche;
	Ensemble<String> droite;
	Double confidence;
	
	
	
	public RegleAssociation(Ensemble<String> gauche, Ensemble<String> droite, Double confidence) {
		this.gauche = gauche;
		this.droite = droite;
		this.confidence = confidence;
	}


	public static Regles extraitre_regles_association(Itemsets L, double min_conf) {
		Regles regles = new Regles();
		Supports supports = new Supports();
		Itemsets L_multiples = new Itemsets(); 
		// prendre les itemsets multiples (taille > 1)
		for(Ensemble<String> itemset : L) {
			supports.ajouter(itemset);
			if (itemset.size() > 1) {
				L_multiples.add(itemset);
			}
		}
		
		// former les regles associations depuis L_mult, en ignorant celles avec conf < min_conf
		for(Ensemble<String> itemset : L_multiples) {
			ArrayList<Ensemble<String>> sousEnsembles = itemset.sousEnsembles();
			for(Ensemble<String> subset : sousEnsembles) {
				Ensemble<String> A = subset;
				Ensemble<String> B = itemset.substract(subset);
				double sup_A = supports.get(A);
				double sup_A_B = supports.get(itemset);
				double conf = sup_A_B / sup_A;
				if(conf >= min_conf) {
					regles.add(new RegleAssociation(A, B, conf));
				}
			}
		}
		
		return regles;
	}
	
	@Override
	public String toString() {
		return gauche.toString_simple() + "\t==>\t" + droite.toString_simple() + "\t("+confidence+")";
	}
	
	public static class Regles extends ArrayList<RegleAssociation>{
		private static final long serialVersionUID = 1L;
		@Override
		public String toString() {
			String text = "";
			for(RegleAssociation regle : this) {
				text += regle.toString() + "\n";
			}
			return text;
		}
	}
}
