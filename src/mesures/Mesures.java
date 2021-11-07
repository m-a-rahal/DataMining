package mesures;

import java.util.ArrayList;
import java.util.Collections;

import data.Dataset;

public class Mesures {
	Dataset dataset;
	public Mesures(Dataset dataset) {
		this.dataset = dataset;
	}
	
	// calculer la moyenne d'un attribut (avec indice)
	public double moyenne(int indice_attribut) {
		double moyenne = 0;
		for (int i = 0; i < dataset.n; i++) {
			moyenne += dataset.data[i][indice_attribut];
		}
		return moyenne / dataset.n;
	}

	// calculer la variance d'un attribut (avec indice)
	public double variance(int indice_attribut) {
		double moyenne = moyenne(indice_attribut);
		double variance = 0;
		for (int i = 0; i < dataset.n; i++) {
			variance += Math.pow(dataset.data[i][indice_attribut] - moyenne, 2);
		}
		return variance / dataset.n;
	}

	// calculer l'écart type d'un attribut (avec indice)
	public double ecartType(int indice_attribut) {
		return Math.sqrt(variance(indice_attribut));
	}

	// calculer le mode d'un attribut (avec indice)
	public double mode(int indice_attribut) throws Exception {
		throw new Exception("calcul du mode non implémenté");
	}

	// calculer la médiane d'un attribut (avec indice)
	public double mediane(int indice_attribut) throws Exception {
		ArrayList<Double> vecteur = new ArrayList<>(dataset.n);
		for (int i = 0; i < dataset.n; i++) {
			vecteur.add(dataset.data[i][indice_attribut]);
		}
		Collections.sort(vecteur);
		// si la taile de la liste est pair, retourner l'élement x(n/2), sinon la moyenne des deux élements au milieu
		int n = vecteur.size();
		if (n % 2 == 1) {
			return (vecteur.get(n/2) + vecteur.get(n/2 - 1)) / 2;
		} else {
			return vecteur.get(n/2);
		}
	}
}
