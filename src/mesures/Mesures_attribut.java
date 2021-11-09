package mesures;

import java.util.ArrayList;
import java.util.Collections;

import data.Dataset;

public class Mesures_attribut {
	/** Mesures relatives a une colonne du dataset*/
	Dataset dataset;
	int indice_attribut;
	String nom_attribut;
	// valeurs précalculés — utilisés pour ne pas refaire les calcules à chaque fois
	// ATTENTION: une fois la base de donné changée, il faut associer un nouveau objet mesures a la base de données!
	private Double max = null;
	private Double min = null;
	private Double med = null;
	private Double mod = null;
	private Double moy = null;
	private Double var = null;
	//--------------------------------------------------------------------------------------------------------------

	public Mesures_attribut(Dataset dataset, int indice_attribut) {
		this.dataset = dataset;
		this.indice_attribut = indice_attribut;
		this.nom_attribut = dataset.col_names[indice_attribut];
	}
	
	// calculer la moyenne d'un attribut (avec indice)
	public double moyenne() {
		if (moy != null)
			return moy;
		moy = 0.0;
		for (int i = 0; i < dataset.n; i++) {
			moy += dataset.data[i][indice_attribut];
		}
		moy /= dataset.n;
		return moy;
	}

	// calculer la variance d'un attribut (avec indice)
	public double variance() {
		if (var != null)
			return var;
		double moyenne = moyenne();
		var = 0.0;
		for (int i = 0; i < dataset.n; i++) {
			var += Math.pow(dataset.data[i][indice_attribut] - moyenne, 2);
		}
		var /= dataset.n;
		return var;
	}

	// calculer l'écart type d'un attribut (avec indice)
	public double ecartType() {
		return Math.sqrt(variance());
	}

	// calculer le milieu de l'étendu d'un attribut
	public double milieu() {
		return (max()+min())/2;
	}

	// calculer le mode d'un attribut (avec indice)
	public double mode() throws Exception {
		/** fait une discrétization des donnés avec des intervales de longeure = w
		 * tel que w = (max-min)/k
		 * k = round(   5*log10(nombre de valeurs)  )
		 * le mode est donc le milieu de l'intervale ayant la plus grande frequence
		*/
		if (mod != null)
			return mod;

		int k = (int) Math.round(5 * Math.log10(dataset.n));
		double max = max();
		double min = min();
		double w = (max - min) / k;
		int[] frequences = new int[k];
		for (int i = 0; i < dataset.n; i++) {
			int indice = (int) Math.floor((dataset.data[i][indice_attribut] - min) / w);
			frequences[Math.min(indice,k-1)]++; // l'indice doit être < k, ça peut arriver dans le cas ou valeure = max
		}
		int indice_max = 0;
		for (int i = 1; i < frequences.length; i++) {
			if (frequences[i] > frequences[indice_max]) {
				indice_max = i;
			}
		}
		mod = min + (indice_max + 0.5) * w;
		return mod;
	}

	// calculer la médiane d'un attribut (avec indice)
	public double mediane() throws Exception {
		if (med != null)
			return med;
		double med;
		ArrayList<Double> vecteur = new ArrayList<>(dataset.n);
		for (int i = 0; i < dataset.n; i++) {
			vecteur.add(dataset.data[i][indice_attribut]);
		}
		Collections.sort(vecteur);
		// si la taile de la liste est pair, retourner l'élement x(n/2), sinon la moyenne des deux élements au milieu
		int n = vecteur.size();
		if (n % 2 == 1) {
			med = (vecteur.get(n/2) + vecteur.get(n/2 - 1)) / 2;
		} else {
			med =  vecteur.get(n/2);
		}
		return med;
	}

	public double min() {
		if (min != null)
			return min;
		min = dataset.data[0][indice_attribut];
		for (int i = 1; i < dataset.n; i++) {
			if (dataset.data[i][indice_attribut] < min) {
				min = dataset.data[i][indice_attribut];
			}
		}
		return min;
	}

	public double max() {
		if (max != null)
			return max;
		max = dataset.data[0][indice_attribut];
		for (int i = 1; i < dataset.n; i++) {
			if (dataset.data[i][indice_attribut] > max) {
				max = dataset.data[i][indice_attribut];
			}
		}
		return max;
	}
	
	@Override
	public String toString() {
		String text = nom_attribut + ":\n";
		try {
			text += "\t - moyenne   = " + moyenne() + "\n";
			text += "\t - ecartType = " + ecartType() + "\n";
			text += "\t - variance  = " + variance() + "\n";
			text += "\t - mediane   = " + mediane() + "\n";
			text += "\t - mode      = " + mode() + "\n";
			text += "\t - max       = " + max() + "\n";
			text += "\t - min       = " + min() + "\n";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return text;
	}
}
