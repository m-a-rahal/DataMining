package motifs_frequents_et_regles;

import java.util.ArrayList;

import data.Dataset;

public class Regle {
	Ensemble<String> gauche;
	Ensemble<String> droite;
	Double confidence;
	Double lift;
	public static int POSITIVE = 1, NEGATIVE = -1;



	public Regle(Ensemble<String> gauche, Ensemble<String> droite, Double confidence, double lift) {
		this.gauche = gauche;
		this.droite = droite;
		this.confidence = confidence;
		this.lift = lift;
	}


	public static Regles regles_correlation(Regles regles, int signe) {
		assert signe == POSITIVE || signe == NEGATIVE : "le signe doit être soit 1 pour les règles positives ou -1 pour les règles négatives";
		Regles regles_corr = new Regles(regles.nbr_totale_instances);
		for(Regle regle : regles_correlation(regles)) {
			if ((regle.lift > 1.0 && signe == POSITIVE) || (regle.lift < 1.0 && signe == NEGATIVE)) {
				regles_corr.add(regle);
			}
		}
		return regles_corr;
	}

	public static Regles regles_correlation(Regles regles) {
		Regles regles_corr = new Regles(regles.nbr_totale_instances);
		for(Regle regle : regles) {
			if (regle.lift > 1.0 || regle.lift < 1.0) {
				regles_corr.add(regle);
			}
		}
		return regles_corr;
	}
	
	public static Regles regles_correlation(Itemsets L, double min_conf) {
		return regles_correlation(regles_association(L, min_conf));
	}

	public static Regles regles_association(Itemsets L, double min_conf) {
		Regles regles = new Regles(L.nbr_totale_instances);
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
					A.support = (int) sup_A; // enregistrer les supports des ensembles, utile pour calculer les règles de corrélation
					B.support = supports.get(B);
					double lift = conf / B.support * regles.nbr_totale_instances;
					regles.add(new Regle(A, B, conf, lift));
				}
			}
		}

		return regles;
	}



	@Override
	public String toString() {
		return gauche.toString_simple() + "\t==>\t" + droite.toString_simple() + "\t(conf="+Dataset.arrondi(confidence)+"\tlift="+Dataset.arrondi(lift)+"\tsupp="+(int)(confidence * gauche.support)+")";
	}

	public static class Regles extends ArrayList<Regle>{
		private static final long serialVersionUID = 1L;
		public int nbr_totale_instances;

		public Regles(int nbr_totale_instances) {
			this.nbr_totale_instances = nbr_totale_instances;
		}
		
		public Regles regles_corr_positives() {
			Regles regles = new Regles(nbr_totale_instances);
			for (Regle regle : this) {
				if(regle.lift > 1.0)
					regles.add(regle);
			}
			return regles;
		}
		public Regles regles_corr_negatives() {
			Regles regles = new Regles(nbr_totale_instances);
			for (Regle regle : this) {
				if(regle.lift < 1.0)
					regles.add(regle);
			}
			return regles;
		}
		public Regles regles_corr_indepandantes() {
			Regles regles = new Regles(nbr_totale_instances);
			for (Regle regle : this) {
				if(regle.lift == 0)
					regles.add(regle);
			}
			return regles;
		}
		
		
		@Override
		public String toString() {
			String text = "";
			for(Regle regle : this) {
				text += regle.toString() + "\n";
			}
			return text;
		}
	}
}
